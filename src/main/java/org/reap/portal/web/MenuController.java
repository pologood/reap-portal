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
import org.reap.portal.common.Fields;
import org.reap.portal.domain.Menu;
import org.reap.portal.domain.MenuRepository;
import org.reap.portal.service.AuthorityService;
import org.reap.portal.vo.Function;
import org.reap.support.Result;
import org.reap.util.Assert;
import org.reap.util.FunctionalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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

	/** @apiDefine Menu 菜单维护 */

	/**
	 * @api {post} /menu/{id} 创建子菜单
	 * @apiName createSubMenu
	 * @apiGroup Menu
	 * @apiParam (PathVariable) {String} id 父菜单 id
	 * @apiParam (Body) {String} name 菜单名
	 * @apiParam (Body) {String} leaf 是否为叶子节点
	 * @apiParam (Body) {String} functionCode 对应的功能码
	 * @apiParam (Body) {String} remark 备注
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object} payload 新建的菜单
	 * @apiSuccess (Success) {String} payload.id 菜单 id
	 * @apiSuccess (Success) {String} payload.name 菜单名称
	 * @apiSuccess (Success) {Number} payload.level 菜单层级
	 * @apiSuccess (Success) {Number} payload.sequence 菜单序号
	 * @apiSuccess (Success) {String} payload.functionCode 对应的功能码
	 * @apiSuccess (Success) {String} payload.leaf 是否为叶子结点
	 * @apiSuccess (Success) {String} payload.remark 备注
	 * @apiSuccess (Success) {String} payload.createTime 创建时间
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/menu/{id}", method = RequestMethod.POST)
	public Result<Menu> create(@RequestBody Menu menu, @PathVariable String id) {
		validate(menu);
		Menu parent = FunctionalUtils.orElseThrow(menuRepository.findById(id), ErrorCodes.PARENT_MENU_NOT_EXIST);
		Assert.isTrue(!parent.isLeafMenu(), ErrorCodes.LEAF_MENU_CAN_NOT_BE_PARENT);
		menu.setLevel(parent.getLevel() + 1);
		menu.setParentId(parent.getId());
		menu.setSequence(menuRepository.countByParentId(parent.getId()) + 1);
		menu.setCreateTime(new Date());
		return Result.newResult(menuRepository.save(menu));
	}

	/**
	 * @api {post} /menu 创建菜单
	 * @apiName createMenu
	 * @apiGroup Menu
	 * @apiParam (Body) {String} name 菜单名
	 * @apiParam (Body) {String} leaf 是否为叶子节点
	 * @apiParam (Body) {String} functionCode 对应的功能码
	 * @apiParam (Body) {String} remark 备注
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object} payload 新建的菜单
	 * @apiSuccess (Success) {String} payload.id 菜单 id
	 * @apiSuccess (Success) {String} payload.name 菜单名称
	 * @apiSuccess (Success) {Number} payload.level 菜单层级
	 * @apiSuccess (Success) {Number} payload.sequence 菜单序号
	 * @apiSuccess (Success) {String} payload.functionCode 对应的功能码
	 * @apiSuccess (Success) {String} payload.leaf 是否为叶子结点
	 * @apiSuccess (Success) {String} payload.remark 备注
	 * @apiSuccess (Success) {String} payload.createTime 创建时间
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/menu", method = RequestMethod.POST)
	public Result<Menu> createRootMenu(@RequestBody Menu menu) {
		validate(menu);
		menu.setLevel(1);
		menu.setCreateTime(new Date());
		menu.setSequence(menuRepository.countByParentId(null) + 1);
		return Result.newResult(menuRepository.save(menu));
	}

	/**
	 * @api {post} /menu/{id} 删除菜单
	 * @apiName deleteMenu
	 * @apiGroup Menu
	 * @apiParam (PathVariable) {String} id 菜单 id
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/menu/{id}", method = RequestMethod.DELETE)
	@Transactional
	public Result<?> delete(@PathVariable String id) {
		Menu menu = FunctionalUtils.orElseThrow(menuRepository.findById(id), ErrorCodes.MENU_NOT_EXIST);
		if (menu.isRoot()) {
			menuRepository.deleteByParentId(id);
		}
		menuRepository.delete(menu);
		return Result.newResult();
	}

	/**
	 * @api {put} /menu 更新菜单
	 * @apiName updateMenu
	 * @apiGroup Menu
	 * @apiParam (Body) {String} id 菜单 id
	 * @apiParam (Body) {String} name 菜单名
	 * @apiParam (Body) {String} functionCode 对应的功能码
	 * @apiParam (Body) {String} sequence 序号
	 * @apiParam (Body) {String} remark 备注
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object} payload 新建的菜单
	 * @apiSuccess (Success) {String} payload.id 菜单 id
	 * @apiSuccess (Success) {String} payload.name 菜单名称
	 * @apiSuccess (Success) {Number} payload.level 菜单层级
	 * @apiSuccess (Success) {Number} payload.sequence 菜单序号
	 * @apiSuccess (Success) {String} payload.functionCode 对应的功能码
	 * @apiSuccess (Success) {String} payload.leaf 是否为叶子结点
	 * @apiSuccess (Success) {String} payload.remark 备注
	 * @apiSuccess (Success) {String} payload.createTime 创建时间
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/menu", method = RequestMethod.PUT)
	public Result<Menu> update(@RequestBody Menu menu) {
		Menu persisted = FunctionalUtils.orElseThrow(menuRepository.findById(menu.getId()), ErrorCodes.MENU_NOT_EXIST);
		persisted.setName(menu.getName());
		persisted.setRemark(menu.getRemark());
		persisted.setSequence(menu.getSequence());
		persisted.setLeaf(menu.getLeaf());
		persisted.setFunctionCode(menu.getFunctionCode());
		validate(persisted);
		return Result.newResult(menuRepository.save(persisted));
	}

	/**
	 * @api {post} /menu/move/{dragMenuId}/{targetMenuId}/{position} 移动菜单
	 * @apiName moveMenu
	 * @apiGroup Menu
	 * @apiParam (PathVariable) {String} dragMenuId 拖动的菜单 id
	 * @apiParam (PathVariable) {String} targetMenuId 目标菜单 id
	 * @apiParam (PathVariable) {Number} position 位置 -1 目标菜单前 0 目标菜单中 1 目标菜单后
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@Transactional
	@RequestMapping(path = "/menu/move/{drageMenuId}/{targetMenuId}/{position}", method = RequestMethod.POST)
	public Result<?> move(@PathVariable("drageMenuId") String drageMenuId,
			@PathVariable("targetMenuId") String targetMenuId, @PathVariable("position") int position) {
		Menu targetMenu = FunctionalUtils.orElseThrow(menuRepository.findById(targetMenuId), ErrorCodes.MENU_NOT_EXIST);
		Menu dragMenu = FunctionalUtils.orElseThrow(menuRepository.findById(drageMenuId), ErrorCodes.MENU_NOT_EXIST);

		if (position == Constants.MOVE_MENU_POSITION_IN) {
			if (!targetMenu.isLeafMenu()) {
				List<Menu> sourceMenuList = menuRepository.findByParentId(dragMenu.getParentId()).stream().filter(
						m -> !m.getId().equals(dragMenu.getId())).sorted(
								Comparator.comparing(Menu::getSequence)).collect(Collectors.toList());
				menuRepository.saveAll(sourceMenuList);
				List<Menu> menus = menuRepository.findByParentId(targetMenu.getId());
				for (int i = 0; i < sourceMenuList.size(); i++) {
					sourceMenuList.get(i).setSequence(i + 1);
				}
				dragMenu.setParentId(targetMenu.getId());
				dragMenu.setSequence(menus.size() + 1);
				menuRepository.save(dragMenu);
			}
		}
		else {
			// 同级菜单移动
			if (ObjectUtils.equals(targetMenu.getParentId(), dragMenu.getParentId())) {
				List<Menu> targetMenus = new ArrayList<>();
				menuRepository.findByParentId(dragMenu.getParentId()).stream().filter(
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
				List<Menu> sourceMenuList = menuRepository.findByParentId(dragMenu.getParentId()).stream().filter(
						m -> !m.getId().equals(dragMenu.getId())).sorted(
								Comparator.comparing(Menu::getSequence)).collect(Collectors.toList());
				for (int i = 0; i < sourceMenuList.size(); i++) {
					sourceMenuList.get(i).setSequence(i + 1);
				}
				menuRepository.saveAll(sourceMenuList);

				List<Menu> targetMenus = new ArrayList<>();
				dragMenu.setParentId(targetMenu.getParentId());
				menuRepository.findByParentId(targetMenu.getParentId()).stream().filter(
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
		return Result.newResult();
	}

	/**
	 * @api {get} /menu/{id} 查询指定 id 的菜单
	 * @apiName getMenuById
	 * @apiGroup Menu
	 * @apiParam (PathVariable) {String} id 菜单 id
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object} payload 新建的菜单
	 * @apiSuccess (Success) {String} payload.id 菜单 id
	 * @apiSuccess (Success) {String} payload.name 菜单名称
	 * @apiSuccess (Success) {Number} payload.level 菜单层级
	 * @apiSuccess (Success) {Number} payload.sequence 菜单序号
	 * @apiSuccess (Success) {String} payload.functionCode 对应的功能码
	 * @apiSuccess (Success) {String} payload.leaf 是否为叶子结点
	 * @apiSuccess (Success) {String} payload.remark 备注
	 * @apiSuccess (Success) {String} payload.createTime 创建时间
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/menu/{id}", method = RequestMethod.GET)
	public Result<Menu> findOne(@PathVariable String id) {
		return Result.newResult(
				FunctionalUtils.orElseThrow(menuRepository.findById(id), ErrorCodes.MENU_NOT_EXIST));
	}

	/**
	 * @api {get} /menus 查询菜单
	 * @apiName getMenus
	 * @apiGroup Menu
	 * @apiParam (QueryString) {Number} [page=0] 页码
	 * @apiParam (QueryString) {Number} [size=10] 每页记录数
	 * @apiParam (QueryString) {String} [name] 菜单名称
	 * @apiParam (QueryString) {Number} [level] 菜单层次
	 * @apiParam (QueryString) {String} [parentMenuId] 父菜单 id
	 * @apiParam (QueryString) {String} [functionCode] 功能码
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Number} payload.totalPages 总页数
	 * @apiSuccess (Success) {Number} payload.totalElements 总记录数
	 * @apiSuccess (Success) {Number} payload.numberOfElements 当前记录数
	 * @apiSuccess (Success) {Object[]} payload.content 菜单列表
	 * @apiSuccess (Success) {String} payload.content.id 菜单 id
	 * @apiSuccess (Success) {String} payload.content.name 菜单名称
	 * @apiSuccess (Success) {Number} payload.content.level 菜单层级
	 * @apiSuccess (Success) {Number} payload.content.sequence 菜单序号
	 * @apiSuccess (Success) {String} payload.content.functionCode 对应的功能码
	 * @apiSuccess (Success) {String} payload.content.leaf 是否为叶子结点
	 * @apiSuccess (Success) {String} payload.content.remark 备注
	 * @apiSuccess (Success) {String} payload.content.createTime 创建时间
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/menus", method = RequestMethod.GET)
	public Result<Page<Menu>> find(@RequestParam int page, @RequestParam int size, Menu spec) {
		Example<Menu> example = Example.of(spec,
				ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths(Fields.LEAF).withStringMatcher(
						StringMatcher.CONTAINING));
		PageRequest pageRequest = (StringUtils.isEmpty(spec.getParentId()))
				? PageRequest.of(page, size, Sort.by(Direction.ASC, Fields.ID))
				: PageRequest.of(page, size, Sort.by(Direction.ASC, Fields.SEQUENCE));
		return Result.newResult(menuRepository.findAll(example, pageRequest));
	}

	/**
	 * @api {get} /portal/functions 查询功能
	 * @apiName getFunctions
	 * @apiGroup Menu
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object[]} payload 功能列表
	 * @apiSuccess (Success) {String} payload.id 功能 id
	 * @apiSuccess (Success) {String} payload.serviceId 归属系统
	 * @apiSuccess (Success) {String} payload.code 功能码
	 * @apiSuccess (Success) {String} payload.name 功能名称
	 * @apiSuccess (Success) {String} payload.type 类型 'M' 菜单 'O' 操作
	 * @apiSuccess (Success) {String} payload.action 动作
	 * @apiSuccess (Success) {String} payload.remark 备注
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/portal/functions", method = RequestMethod.GET)
	public Result<List<Function>> findFunctions() {
		return Result.newResult(authorityService.fetchFunctions());
	}

	/**
	 * @api {get} /menu/tree 查询菜单树
	 * @apiDescription 查询所有的菜单并按照树型结构组织
	 * @apiName getMenuTree
	 * @apiGroup Menu
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object[]} payload 菜单列表
	 * @apiSuccess (Success) {String} payload.id 菜单 id
	 * @apiSuccess (Success) {String} payload.name 菜单名称
	 * @apiSuccess (Success) {Number} payload.level 菜单层级
	 * @apiSuccess (Success) {Number} payload.sequence 菜单序号
	 * @apiSuccess (Success) {String} payload.functionCode 对应的功能码
	 * @apiSuccess (Success) {String} payload.leaf 是否为叶子结点
	 * @apiSuccess (Success) {String} payload.remark 备注
	 * @apiSuccess (Success) {String} payload.createTime 创建时间
	 * @apiSuccess (Success) {Object[]} payload.children 子菜单
	 * @apiSuccess (Success) {String} payload.children.id 子菜单 id
	 * @apiSuccess (Success) {String} payload.children.name 子菜单名称
	 * @apiSuccess (Success) {Number} payload.children.level 子菜单层级
	 * @apiSuccess (Success) {Number} payload.children.sequence 子菜单序号
	 * @apiSuccess (Success) {String} payload.children.functionCode 子菜单对应的功能码
	 * @apiSuccess (Success) {String} payload.children.leaf 子菜单是否为叶子结点
	 * @apiSuccess (Success) {String} payload.children.remark 子菜单备注
	 * @apiSuccess (Success) {String} payload.children.createTime 子菜单创建时间
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/menus/tree", method = RequestMethod.GET)
	public Result<List<Menu>> menuTree() {
		List<Menu> menus = menuRepository.findAll();
		Map<String, Menu> menuMapping = menus.stream().collect(Collectors.toMap(Menu::getId, m -> m));
		for (Menu m : menus) {
			if (m.getParentId() != null) {
				menuMapping.get(m.getParentId()).addChildren(m);
			}
		}
		return Result.newResult(menus.stream().filter(m -> m.getParentId() == null).sorted(
				Comparator.comparing(Menu::getSequence)).collect(Collectors.toList()));
	}

	private void validate(Menu menu) {
		Assert.isTrue(!(menu.isLeafMenu() && StringUtils.isEmpty(menu.getFunctionCode())),
				ErrorCodes.LEAF_MENU_MUST_CONTAINS_FUNCTIONS);
		if (StringUtils.isNotEmpty(menu.getFunctionCode())) {
			Assert.isTrue(authorityService.fetchFunctions().stream().anyMatch(
					f -> f.getCode().equals(menu.getFunctionCode())), ErrorCodes.FUNCTION_NOT_EXISTS);
		}
	}
}
