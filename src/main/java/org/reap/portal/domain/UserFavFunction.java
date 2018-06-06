
package org.reap.portal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.reap.portal.vo.Function;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author hehaiwei
 *
 */
@Entity
public class UserFavFunction {

	@Id
	@GeneratedValue
	private Long id;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
