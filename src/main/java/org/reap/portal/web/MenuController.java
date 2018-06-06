/*
 * Copyright (c) 2018-present the original author or authors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.reap.portal.web;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.reap.portal.common.Constants;
import org.reap.portal.common.ErrorCodes;
import org.reap.portal.domain.Menu;
import org.reap.portal.domain.MenuRepository;
import org.reap.portal.service.AuthorityService;
import org.reap.portal.vo.Function;
import org.reap.portal.vo.QueryMenuSpec;
import org.reap.support.DefaultResult;
import org.reap.support.Result;
import org.reap.util.Assert;
import org.reap.util.FunctionalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuController {

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private AuthorityService authorityService;

	@RequestMapping(path = "/menu/{id}", method = RequestMethod.POST)
	public Result<Menu> create(@RequestBody Menu menu, @PathVariable String id) {
		validate(menu);
		Menu parent = FunctionalUtils.orElseThrow(menuRepository.findById(id), ErrorCodes.PARENT_MENU_NOT_EXIST);
		Assert.isTrue(!parent.isLeaf(), ErrorCodes.LEAF_MENU_CAN_NOT_BE_PARENT);
		menu.setLevel(parent.getLevel() + 1);
		menu.setParent(parent);
		menu.setSequence(menuRepository.countByParent(parent) + 1);
		menu.setCreateTime(new Date());
		return DefaultResult.newResult(menuRepository.save(menu));
	}

	@RequestMapping(path = "/menu", method = RequestMethod.POST)
	public Result<Menu> createRootMenu(@RequestBody Menu menu) {
		validate(menu);
		menu.setLevel(1);
		menu.setCreateTime(new Date());
		menu.setSequence(menuRepository.countByParent(null) + 1);
		return DefaultResult.newResult(menuRepository.save(menu));
	}

	@RequestMapping(path = "/menu/{id}", method = RequestMethod.DELETE)
	@Transactional
	public Result<?> delete(@PathVariable String id) {
		Menu menu = FunctionalUtils.orElseThrow(menuRepository.findById(id), ErrorCodes.MENU_NOT_EXIST);
		if (menu.isRoot()) {
			menuRepository.deleteByParent(menu);
		}
		menuRepository.delete(menu);
		return DefaultResult.newResult();
	}

	@RequestMapping(path = "/menu", method = RequestMethod.PUT)
	public Result<Menu> update(@RequestBody Menu menu) {
		Menu persisted = FunctionalUtils.orElseThrow(menuRepository.findById(menu.getId()), ErrorCodes.MENU_NOT_EXIST);
		persisted.setName(menu.getName());
		persisted.setRemark(menu.getRemark());
		persisted.setSequence(menu.getSequence());
		persisted.setLeaf(menu.getLeaf());
		persisted.setFunctionCode(menu.getFunctionCode());
		validate(persisted);
		return DefaultResult.newResult(menuRepository.save(persisted));
	}

	// TODO 代码冗余、后续重构
	@Transactional
	@RequestMapping(path = "/menu/move/{drageMenuId}/{targetMenuId}/{position}", method = RequestMethod.POST)
	public Result<?> move(@PathVariable("drageMenuId") String drageMenuId,
			@PathVariable("targetMenuId") String targetMenuId, @PathVariable("position") int position) {
		Menu targetMenu = FunctionalUtils.orElseThrow(menuRepository.findById(targetMenuId), ErrorCodes.MENU_NOT_EXIST);
		Menu dragMenu = FunctionalUtils.orElseThrow(menuRepository.findById(drageMenuId), ErrorCodes.MENU_NOT_EXIST);

		if (position == Constants.MOVE_MENU_POSITION_IN) {
			if (!targetMenu.isLeaf()) {
				List<Menu> sourceMenuList = menuRepository.findByParent(dragMenu.getParent()).stream().filter(
						m -> !m.getId().equals(dragMenu.getId())).sorted(
								Comparator.comparing(Menu::getSequence)).collect(Collectors.toList());
				menuRepository.saveAll(sourceMenuList);
				List<Menu> menus = menuRepository.findByParent(targetMenu);
				for (int i = 0; i < sourceMenuList.size(); i++) {
					sourceMenuList.get(i).setSequence(i + 1);
				}
				dragMenu.setParent(targetMenu);
				dragMenu.setSequence(menus.size() + 1);
				menuRepository.save(dragMenu);
			}
		}
		else {
			// 同级菜单移动
			if (ObjectUtils.equals(targetMenu.getParent(), dragMenu.getParent())) {
				List<Menu> targetMenus = new ArrayList<>();
				menuRepository.findByParent(dragMenu.getParent()).stream().filter(
						m -> !m.getId().equals(dragMenu.getId())).sorted(
								Comparator.comparing(Menu::getSequence)).forEach((m) -> {
									if (m.getId().equals(targetMenu.getId())) {
										if (position == Constants.MOVE_MENU_POSITION_BEFORE) {
											targetMenus.add(dragMenu);
											targetMenus.add(m);
										}
										else {
											targetMenus.add(m);
											targetMenus.add(dragMenu);
										}
									}
									else {
										targetMenus.add(m);
									}
								});

				for (int i = 0; i < targetMenus.size(); i++) {
					targetMenus.get(i).setSequence(i + 1);
				}
				menuRepository.saveAll(targetMenus);
			}
			// 移动到其它菜单下
			else {
				List<Menu> sourceMenuList = menuRepository.findByParent(dragMenu.getParent()).stream().filter(
						m -> !m.getId().equals(dragMenu.getId())).sorted(
								Comparator.comparing(Menu::getSequence)).collect(Collectors.toList());
				for (int i = 0; i < sourceMenuList.size(); i++) {
					sourceMenuList.get(i).setSequence(i + 1);
				}
				menuRepository.saveAll(sourceMenuList);

				List<Menu> targetMenus = new ArrayList<>();
				dragMenu.setParent(targetMenu.getParent());
				menuRepository.findByParent(targetMenu.getParent()).stream().filter(
						m -> !m.getId().equals(dragMenu.getId())).sorted(
								Comparator.comparing(Menu::getSequence)).forEach(m -> {
									if (m.getId().equals(targetMenu.getId())) {
										if (position == Constants.MOVE_MENU_POSITION_BEFORE) {
											targetMenus.add(dragMenu);
											targetMenus.add(m);
										}
										else {
											targetMenus.add(m);
											targetMenus.add(dragMenu);
										}
									}
									else {
										targetMenus.add(m);
									}
								});
				for (int i = 0; i < targetMenus.size(); i++) {
					targetMenus.get(i).setSequence(i + 1);
				}
				menuRepository.saveAll(targetMenus);
			}
		}
		return DefaultResult.newResult();
	}

	@RequestMapping(path = "/menu/{id}", method = RequestMethod.GET)
	public Result<Menu> findOne(@PathVariable String id) {
		return DefaultResult.newResult(
				FunctionalUtils.orElseThrow(menuRepository.findById(id), ErrorCodes.MENU_NOT_EXIST));
	}

	@RequestMapping(path = "/menus", method = RequestMethod.GET)
	public Result<Page<Menu>> find(@RequestParam int page, @RequestParam int size, QueryMenuSpec spec) {
		return DefaultResult.newResult(menuRepository.findAll(spec.toSpecification(), PageRequest.of(page, size)));
	}

	@RequestMapping(path = "/portal/functions", method = RequestMethod.GET)
	public Result<List<Function>> findFunctions() {
		return DefaultResult.newResult(authorityService.fetchFunctions());
	}

	@RequestMapping(path = "/menus/tree", method = RequestMethod.GET)
	public Result<List<Menu>> menuTree() {
		List<Menu> menus = menuRepository.findAll();
		Map<String, Menu> menuMapping = menus.stream().collect(Collectors.toMap(Menu::getId, m -> m));
		for (Menu m : menus) {
			if (m.getParent() != null) {
				menuMapping.get(m.getParent().getId()).addChildren(m);
			}
		}
		return DefaultResult.newResult(menus.stream().filter((m) -> m.getParent() == null).sorted(
				Comparator.comparing(Menu::getSequence)).collect(Collectors.toList()));
	}

	private void validate(Menu menu) {
		Assert.isTrue(!(menu.isLeaf() && StringUtils.isEmpty(menu.getFunctionCode())),
				ErrorCodes.LEAF_MENU_MUST_CONTAINS_FUNCTIONS);
		if (StringUtils.isNotEmpty(menu.getFunctionCode())) {
			Assert.isTrue(authorityService.fetchFunctions().stream().anyMatch(
					f -> f.getCode().equals(menu.getFunctionCode())), ErrorCodes.FUNCTION_NOT_EXISTS);
		}
	}
}
