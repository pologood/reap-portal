
package org.reap.portal.domain;

import java.util.List;

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
public class UserSetting {

	@Id
	@GeneratedValue
	private String id;

	private String userId;

	private String homeFunctionCode;

	@Transient
	private Function homeFunction;

	@Transient
	private List<UserFavFunction> favFunctions;

	public void setHomeFunction(Function homeFunction) {
		this.homeFunction = homeFunction;
	}

	public Function getHomeFunction() {
		return homeFunction;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getHomeFunctionCode() {
		return homeFunctionCode;
	}

	public void setHomeFunctionCode(String homeFunctionCode) {
		this.homeFunctionCode = homeFunctionCode;
	}

	public List<UserFavFunction> getFavFunctions() {
		if (null != favFunctions) {
			favFunctions.stream().forEach(u -> u.setUserSettingId(this.getId()));
		}
		return favFunctions;
	}

	public void setFavFunctions(List<UserFavFunction> favFunctions) {
		this.favFunctions = favFunctions;
	}

}
