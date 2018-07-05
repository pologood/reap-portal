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

import org.reap.portal.common.ErrorCodes;
import org.reap.portal.domain.UserFavFunctionRepository;
import org.reap.portal.domain.UserSetting;
import org.reap.portal.domain.UserSettingRepository;
import org.reap.support.DefaultResult;
import org.reap.support.Result;
import org.reap.util.FunctionalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户设置.
 * 
 * @author hehaiwei
 * @since 1.0
 */
@RestController
public class UserSettingController {

	@Autowired
	private UserSettingRepository userSettingRepository;
	
	@Autowired
	private UserFavFunctionRepository userFavFunctionRepository;

	/** @apiDefine UserSetting 用户设置 */

	/**
	 * @api {post} /setting 创建用户设置
	 * @apiName createUserSetting
	 * @apiGroup UserSetting
	 * @apiParam (Body) {String} userId 用户 id
	 * @apiParam (Body) {String} homeFunctionCode 首页功能码
	 * @apiParam (Body) {Object[]} favFunctions 常用功能
	 * @apiParam (Body) {String} favFunctions.functionCode 功能码
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object} payload 新建的用户设置
	 * @apiSuccess (Success) {String} payload.userId 用户 id
	 * @apiSuccess (Success) {String} payload.homeFunctionCode 首页功能码
	 * @apiSuccess (Success) {Object[]} payload.favFunctions 常用功能列表
	 * @apiSuccess (Success) {String} payload.favFunctions.id 常用功能 id
	 * @apiSuccess (Success) {String} payload.favFunctions.functionCode 常用功能对应的功能码
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/setting", method = RequestMethod.POST)
	public Result<UserSetting> create(@RequestBody UserSetting userSetting) {
		return DefaultResult.newResult(userSettingRepository.save(userSetting));
	}

	/**
	 * @api {put} /setting 更新用户设置
	 * @apiName updateUserSetting
	 * @apiGroup UserSetting
	 * @apiParam (Body) {String} id 用户设置 id
	 * @apiParam (Body) {String} userId 用户 id
	 * @apiParam (Body) {String} homeFunctionCode 首页功能码
	 * @apiParam (Body) {Object[]} favFunctions 常用功能
	 * @apiParam (Body) {String} favFunctions.functionCode 功能码
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object} payload 新建的用户设置
	 * @apiSuccess (Success) {String} payload.userId 用户 id
	 * @apiSuccess (Success) {String} payload.homeFunctionCode 首页功能码
	 * @apiSuccess (Success) {Object[]} payload.favFunctions 常用功能列表
	 * @apiSuccess (Success) {String} payload.favFunctions.id 常用功能 id
	 * @apiSuccess (Success) {String} payload.favFunctions.functionCode 常用功能对应的功能码
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/setting", method = RequestMethod.PUT)
	@Transactional
	public Result<UserSetting> update(@RequestBody UserSetting userSetting) {
		UserSetting persisted = FunctionalUtils.orElseThrow(userSettingRepository.findById(userSetting.getId()),
				ErrorCodes.USER_SETTING_NOT_EXISTS);
		persisted.setHomeFunctionCode(userSetting.getHomeFunctionCode());
		userFavFunctionRepository.deleteByUserSettingId(userSetting.getId());
		userFavFunctionRepository.saveAll(userSetting.getFavFunctions());
		persisted.setFavFunctions(userSetting.getFavFunctions());
		return DefaultResult.newResult(userSettingRepository.save(persisted));
	}
}
