
package org.reap.portal.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.reap.portal.common.Constants;
import org.reap.portal.vo.Function;

/**
 * @author hehaiwei
 *
 */
@Entity
@Table(schema=Constants.PORTAL_SCHEMA)
public class UserFavFunction {

	@Id
	@GeneratedValue
	private String id;

	private String functionCode;

	private String userSettingId;

	@Transient
	private Function function;

	public void setFunction(Function function) {
		this.function = function;
	}

	public Function getFunction() {
		return function;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getUserSettingId() {
		return userSettingId;
	}

	public void setUserSettingId(String userSettingId) {
		this.userSettingId = userSettingId;
	}
}
