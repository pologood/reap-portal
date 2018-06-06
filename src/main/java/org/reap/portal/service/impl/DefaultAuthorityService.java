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

package org.reap.portal.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.reap.portal.common.Fields;
import org.reap.portal.domain.Menu;
import org.reap.portal.domain.MenuRepository;
import org.reap.portal.domain.UserSetting;
import org.reap.portal.domain.UserSettingRepository;
import org.reap.portal.service.AuthorityService;
import org.reap.portal.vo.Function;
import org.reap.portal.vo.MenuTree;
import org.reap.portal.vo.User;
import org.reap.support.DefaultResult;
import org.reap.support.Result;
import org.reap.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author hehaiwei
 * @since 1.0
 */
@Component
public class DefaultAuthorityService implements AuthorityService {
	
	@Autowired
	private MenuRepository menuRepository;
	
	@Autowired
	private UserSettingRepository userSettingRepository;
	
	@Autowired
    RestTemplate restTemplate;
	
	@Value ("${api.logon}")
	private String api_logon;
	
	@Value ("${api.fetchFunctions}")
	private String api_fetchFunctions;

	@Override
	public User authentication(User user) {
		LinkedMultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.add(Fields.USER_NAME, user.getUsername());
		request.add(Fields.PASSWORD, user.getPassword());
		Result<User> result = restTemplate.exchange(api_logon, HttpMethod.POST, new HttpEntity<MultiValueMap<String, String>>(request, null), 
				new ParameterizedTypeReference<DefaultResult<User>>() {}).getBody();
		Assert.isTrue(result.isSuccess(), result.getResponseCode(), result.getResponseMessage());
		return result.getPayload();
	}
	
	@Override
	public List<Menu> fetchMenuTree() {
		List<Menu> menus = menuRepository.findAll();
		Map<Long, Menu> menuMapping = menus.stream().collect(Collectors.toMap(Menu::getId, m -> m));
		for (Menu m : menus) {
			if (m.getParent() != null) {
				menuMapping.get(m.getParent().getId()).addChildren(m);
			}
		}
		return menus.stream().filter((m) -> m.getParent() == null).sorted(Comparator.comparing(Menu::getSequence)).collect(
				Collectors.toList());
	}

	public MenuTree fetchUserMenuTree(User user) {
		List<Menu> menus = menuRepository.findAll();
		return new MenuTree(menus, fetchUserFunctions(user));
	}

	@Override
	public List<Function> fetchFunctions() {
		Result<List<Function>> result = restTemplate.exchange(api_fetchFunctions, HttpMethod.GET, null, 
				new ParameterizedTypeReference<DefaultResult<List<Function>>>() {}).getBody();
		Assert.isTrue(result.isSuccess(), result.getResponseCode(), result.getResponseMessage());
		return result.getPayload();
	}

	@Override
	public Map<String, Function> fetchUserFunctions(User user) {
		List<Function> functions = new ArrayList<Function>();
		user.getRoles().forEach(role -> functions.addAll(role.getFunctions()));
		Map<String, Function> functionMap = functions.stream().collect(Collectors.toMap(Function::getCode, f -> f));
		return functionMap;
	}
	
	public void setMenuRepository(MenuRepository menuRepository) {
		this.menuRepository = menuRepository;
	}

	@Override
	public UserSetting fetchUserSetting(User user) {
		Collection<Function> functions = fetchUserFunctions(user).values();
		UserSetting setting = userSettingRepository.findByUserId(user.getId()).orElse(null);
		if(setting == null)
			return null;
		setting.setHomeFunction(functions.stream().filter((f) -> f.getCode().equals(setting.getHomeFunctionCode())).findFirst().orElse(null));
		setting.getFavFunctions().forEach(
				favFunction -> 
					favFunction.setFunction(functions.stream().filter((f) -> f.getCode().equals(favFunction.getFunctionCode())).findFirst().orElse(null))
				);
		return setting;
	}

}
