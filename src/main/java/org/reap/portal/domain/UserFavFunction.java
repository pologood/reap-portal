
package org.reap.portal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.reap.portal.vo.Function;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author hehaiwei
 *
 */
@Entity
public class UserFavFunction {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	@Column
	private String functionCode;

	@ManyToOne
	@JsonIgnore
	private UserSetting userSetting;

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

	public UserSetting getUserSetting() {
		return userSetting;
	}

	public void setUserSetting(UserSetting userSetting) {
		this.userSetting = userSetting;
	}
}
