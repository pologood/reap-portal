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

	/** @apiDefine Session 会话管理 */

	/**
	 * @api {post} /logon 用户登录
	 * @apiName logon
	 * @apiGroup Session
	 * @apiParam (Body) {String} username 用户名
	 * @apiParam (Body) {String} password 密码
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object} payload 会话
	 * @apiSuccess (Success) {String} payload.token 用于跟踪会话的 token
	 * @apiSuccess (Success) {Object} payload.user 用户信息
	 * @apiSuccess (Success) {String} payload.user.username 用户名
	 * @apiSuccess (Success) {String} payload.user.name 姓名
	 * @apiSuccess (Success) {String} payload.user.email 邮箱
	 * @apiSuccess (Success) {String} payload.user.phoneNo 手机
	 * @apiSuccess (Success) {String} payload.user.createTime 创建时间
	 * @apiSuccess (Success) {Object} payload.user.org 归属机构
	 * @apiSuccess (Success) {String} payload.user.org.id 机构 id
	 * @apiSuccess (Success) {String} payload.user.org.name 机构名称
	 * @apiSuccess (Success) {String} payload.user.org.code 机构代码
	 * @apiSuccess (Success) {String} payload.user.org.createTime 创建时间
	 * @apiSuccess (Success) {Object[]} payload.user.roles 岗位
	 * @apiSuccess (Success) {String} payload.user.roles.id 岗位 id
	 * @apiSuccess (Success) {String} payload.user.roles.name 岗位名称
	 * @apiSuccess (Success) {String} payload.user.roles.remark 备注
	 * @apiSuccess (Success) {Object[]} payload.user.roles.functions 岗位功能
	 * @apiSuccess (Success) {String} payload.user.roles.functions.id 功能 id
	 * @apiSuccess (Success) {String} payload.user.roles.functions.serviceId 归属系统
	 * @apiSuccess (Success) {String} payload.user.roles.functions.code 功能码
	 * @apiSuccess (Success) {String} payload.user.roles.functions.name 功能名称
	 * @apiSuccess (Success) {String} payload.user.roles.functions.type 功能类型 'M' 菜单 'O' 操作
	 * @apiSuccess (Success) {String} payload.user.roles.functions.action 动作
	 * @apiSuccess (Success) {String} payload.user.roles.functions.remark 备注
	 * @apiSuccess (Success) {Object[]} payload.menus 菜单
	 * @apiSuccess (Success) {String} payload.menus.id 菜单 id
	 * @apiSuccess (Success) {String} payload.menus.name 菜单 名称
	 * @apiSuccess (Success) {Number} payload.menus.level 菜单层级
	 * @apiSuccess (Success) {Number} payload.menus.sequence 菜单序号
	 * @apiSuccess (Success) {String} payload.menus.functionCode 菜单功能码
	 * @apiSuccess (Success) {String} payload.menus.function 菜单对应的功能
	 * @apiSuccess (Success) {String} payload.menus.function.id 功能 id
	 * @apiSuccess (Success) {String} payload.menus.function.serviceId 归属系统
	 * @apiSuccess (Success) {String} payload.menus.function.code 功能码
	 * @apiSuccess (Success) {String} payload.menus.function.name 功能名称
	 * @apiSuccess (Success) {String} payload.menus.function.type 类型 'M' 菜单 'O' 操作
	 * @apiSuccess (Success) {String} payload.menus.function.action 动作
	 * @apiSuccess (Success) {String} payload.menus.function.remark 备注
	 * @apiSuccess (Success) {String} payload.menus.leaf 是否叶子节点 'Y' 是 'N' 否
	 * @apiSuccess (Success) {String} payload.menus.remark 备注
	 * @apiSuccess (Success) {String} payload.menus.createTime 创建时间
	 * @apiSuccess (Success) {Object[]} payload.menus.childrens 子菜单
	 * @apiSuccess (Success) {String} payload.menus.childrens.id 菜单 id
	 * @apiSuccess (Success) {String} payload.menus.childrens.name 菜单 名称
	 * @apiSuccess (Success) {Number} payload.menus.childrens.level 菜单层级
	 * @apiSuccess (Success) {Number} payload.menus.childrens.sequence 菜单序号
	 * @apiSuccess (Success) {String} payload.menus.childrens.functionCode 菜单功能码
	 * @apiSuccess (Success) {String} payload.menus.childrens.leaf 是否叶子节点 'Y' 是 'N' 否
	 * @apiSuccess (Success) {String} payload.menus.childrens.remark 备注
	 * @apiSuccess (Success) {String} payload.menus.childrens.createTime 创建时间
	 * @apiSuccess (Success) {Object[]} payload.menus.childrens.childrens 子菜单
	 * @apiSuccess (Success) {Object} payload.userSetting 用户设置
	 * @apiSuccess (Success) {String} payload.userSetting.id 用户设置 id
	 * @apiSuccess (Success) {Object} payload.userSetting.userId 用户 id
	 * @apiSuccess (Success) {Object} payload.userSetting.homeFunction 首页功能
	 * @apiSuccess (Success) {String} payload.userSetting.homeFunction.id 功能 id
	 * @apiSuccess (Success) {String} payload.userSetting.homeFunction.serviceId 归属系统
	 * @apiSuccess (Success) {String} payload.userSetting.homeFunction.code 功能码
	 * @apiSuccess (Success) {String} payload.userSetting.homeFunction.name 功能名称
	 * @apiSuccess (Success) {String} payload.userSetting.homeFunction.type 类型 'M' 菜单 'O' 操作
	 * @apiSuccess (Success) {String} payload.userSetting.homeFunction.action 动作
	 * @apiSuccess (Success) {String} payload.userSetting.homeFunction.remark 备注
	 * @apiSuccess (Success) {Object[]} payload.userSetting.favFunctions 常用功能
	 * @apiSuccess (Success) {String} payload.userSetting.favFunctions.id 功能 id
	 * @apiSuccess (Success) {String} payload.userSetting.favFunctions.serviceId 归属系统
	 * @apiSuccess (Success) {String} payload.userSetting.favFunctions.code 功能码
	 * @apiSuccess (Success) {String} payload.userSetting.favFunctions.name 功能名称
	 * @apiSuccess (Success) {String} payload.userSetting.favFunctions.type 类型 'M' 菜单 'O' 操作
	 * @apiSuccess (Success) {String} payload.userSetting.favFunctions.action 动作
	 * @apiSuccess (Success) {String} payload.userSetting.favFunctions.remark 备注
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/logon", method = RequestMethod.POST)
	public Result<Session> logon(@RequestBody User user) {
		user = authorityService.authentication(user);
		MenuTree menuTree = authorityService.fetchUserMenuTree(user);
		String token = securityService.generateToken(user);
		UserSetting setting = authorityService.fetchUserSetting(user);
		return DefaultResult.newResult(new Session(token, user, menuTree, setting));
	}
}
