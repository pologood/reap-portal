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

package org.reap.portal.vo;

import java.util.List;

import org.reap.portal.domain.Menu;
import org.reap.portal.domain.UserSetting;

/**
 * 用户会话值对象.
 * 
 * @author 7cat
 * @since 1.0
 */
public class Session {

	public Session(String token, User user, MenuTree menuTree, UserSetting userSetting) {
		this.token = token;
		this.user = user;
		this.menuTree = menuTree;
		this.userSetting = userSetting;
	}

	private String token;

	private User user;

	private MenuTree menuTree;
	
	private UserSetting userSetting;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Menu> getMenus() {
		return menuTree.getMenus();
	}

	public void setMenuTree(MenuTree menuTree) {
		this.menuTree = menuTree;
	}
	
	public void setUserSetting(UserSetting userSetting) {
		this.userSetting = userSetting;
	}

	public UserSetting getUserSetting() {
		return userSetting;
	}
}
