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

package org.reap.portal.common;

/**
 * 集中定义错误码格式如下: 'REAP【PO】【2位子模块】【2位错误码】'
 * 
 * <pre>
 * REAPPO0001
 * </pre>
 * 
 * @author 7cat
 * @since 1.0
 */
public final class ErrorCodes {

	/** 菜单不存在. */
	public static final String MENU_NOT_EXIST = "REAPPO0001";

	/** 父菜单不存在. */
	public static final String PARENT_MENU_NOT_EXIST = "REAPPO0002";

	/** 叶子菜单不可设为父菜单. */
	public static final String LEAF_MENU_CAN_NOT_BE_PARENT = "REAPPO0003";

	/** 菜单名重复. */
	public static final String DUPLICATED_MENU_NAME = "REAPPO0004";

	/** 叶子菜单必须关联功能码. */
	public static final String LEAF_MENU_MUST_CONTAINS_FUNCTIONS = "REAPPO0005";

	/** 叶子菜单关联的功能码不存在. */
	public static final String FUNCTION_NOT_EXISTS = "REAPPO0006";

	/** 令牌失效，请重新登录. */
	public static final String TOKEN_EXPIRED = "REAPPO0007";

	/** 令牌错误，请重新登录. */
	public static final String TOKEN_ERROR = "REAPPO0008";

	/** 用户设置不存在. */
	public static final String USER_SETTING_NOT_EXISTS = "REAPPO0009";

	/** 未找到可用的服务. */
	public static final String AVAILABLE_SERVICE_NOT_FOUND = "REAPPO0010";
}
