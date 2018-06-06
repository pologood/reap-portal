
package org.reap.portal.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.reap.portal.vo.Function;

/**
 * @author hehaiwei
 *
 */
@Entity
public class UserSetting {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true, nullable = false)
	private String userId;

	private String homeFunctionCode;

	@Transient
	private Function homeFunction;

	@OneToMany(cascade = CascadeType.ALL)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<UserFavFunction> favFunctions = new ArrayList<UserFavFunction>();

	public void addFavFunctions(UserFavFunction favFunction) {
		favFunctions.add(favFunction);
	}

	public void setHomeFunction(Function homeFunction) {
		this.homeFunction = homeFunction;
	}

	public Function getHomeFunction() {
		return homeFunction;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
		return favFunctions;
	}

	public void setFavFunctions(List<UserFavFunction> favFunctions) {
		this.favFunctions = favFunctions;
		for (UserFavFunction f : favFunctions) {
			f.setUserSetting(this);
		}
	}

}
