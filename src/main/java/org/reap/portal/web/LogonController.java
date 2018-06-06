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

import org.reap.portal.domain.UserSetting;
import org.reap.portal.service.AuthorityService;
import org.reap.portal.service.SecurityService;
import org.reap.portal.vo.MenuTree;
import org.reap.portal.vo.Session;
import org.reap.portal.vo.User;
import org.reap.support.DefaultResult;
import org.reap.support.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户登录.
 * 
 * @author 7cat
 * @since 1.0
 */
@RestController
public class LogonController {

	@Autowired
	private SecurityService securityService;

	@Autowired
	private AuthorityService authorityService;

	@RequestMapping(path = "/logon", method = RequestMethod.POST)
	public Result<Session> logon(@RequestBody User user) {
		user = authorityService.authentication(user);
		MenuTree menuTree = authorityService.fetchUserMenuTree(user);
		String token = securityService.generateToken(user);
		UserSetting setting = authorityService.fetchUserSetting(user);
		return DefaultResult.newResult(new Session(token, user, menuTree, setting));
	}
}
