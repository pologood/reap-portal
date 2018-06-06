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

package org.reap.portal.service;

import java.util.List;
import java.util.Map;

import org.reap.portal.domain.Menu;
import org.reap.portal.domain.UserSetting;
import org.reap.portal.vo.Function;
import org.reap.portal.vo.MenuTree;
import org.reap.portal.vo.User;

/**
 * 系统权限相关的服务.
 * <p>
 * 用户鉴权
 * <p>
 * 查询系统菜单树
 * <p>
 * 查询用户的菜单树
 * <p>
 * 查询所有功能码列表
 * <p>
 * 查询用户的功能码列表
 * <p>
 * 查询用户设置
 * 
 * @author hehaiwei
 *
 */
public interface AuthorityService {

	/**
	 * 用户鉴权
	 * 
	 * @return
	 */
	User authentication(User user);

	/**
	 * 查询系统的菜单树
	 * 
	 * @return
	 */
	List<Menu> fetchMenuTree();

	/**
	 * 查询用户的菜单树
	 * 
	 * @return
	 */
	MenuTree fetchUserMenuTree(User user);

	/**
	 * 查询所有功能码列表
	 * 
	 * @return
	 */
	List<Function> fetchFunctions();

	/**
	 * 查询用户的功能码列表
	 * 
	 * @return
	 */
	Map<String, Function> fetchUserFunctions(User user);

	/**
	 * 查询用户设置
	 * 
	 * @return
	 */
	UserSetting fetchUserSetting(User user);

}
