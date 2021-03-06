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

package org.reap.portal.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.reap.portal.common.Constants;
import org.reap.portal.vo.Function;

/**
 * @author 7cat
 *
 */
@Entity
@Table(schema=Constants.PORTAL_SCHEMA)
public class Menu implements Comparable<Menu> {

	@Id
	@GeneratedValue
	private String id;

	private String name;

	private String parentId;

	private Integer level;

	private Integer sequence;

	private String functionCode;

	private String leaf = Constants.MENU_IS_NOT_LEAF;

	private String remark;

	private Date createTime;

	@Transient
	private Set<Menu> childrens = new HashSet<>();

	@Transient
	private Function function;
	
	@Transient
	private Menu parent;

	public void addChildren(Menu menu) {
		childrens.add(menu);
	}

	public void addChildrens(List<Menu> menus) {
		childrens.addAll(menus);
	}
	
	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public List<Menu> getChildrens() {
		return childrens.stream().sorted().collect(Collectors.toList());
	}

	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

	public boolean containsLeafChildren() {
		// 叶子节点并且功能不为空
		if (this.isLeafMenu() && this.getFunction() != null) {
			return true;
		}
		new ArrayList<>(childrens).stream().forEach((c) -> {
			if (!c.containsLeafChildren()) {
				childrens.remove(c);
			}
		});
		return !childrens.isEmpty();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Menu other = (Menu) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(Menu o) {
		return this.getSequence().compareTo(o.getSequence());
	}

	@Override
	public String toString() {
		return "Menu [id=" + id + "]";
	}

	public boolean isRoot() {
		return null == parentId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public boolean isLeafMenu() {
		return Constants.MENU_IS_LEAF.equals(leaf);
	}

	public String getLeaf() {
		return leaf;
	}

	public void setLeaf(String leaf) {
		this.leaf = leaf;
	}
	 
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getParentId() {
		return parentId;
	}

	
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	
	public void setChildrens(Set<Menu> childrens) {
		this.childrens = childrens;
	}

	public String getParentName() {
		return parent == null ? null : parent.getName();
	}
}
