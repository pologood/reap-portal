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

package org.reap.portal.vo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.reap.portal.common.Fields;
import org.reap.portal.domain.Menu;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * 菜单模糊查询对象
 * 
 * @author hehaiwei
 *
 */
public class QueryMenuSpec {

	private String name;

	private Integer level;

	private String parentMenuId;

	private String functionCode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getParentMenuId() {
		return parentMenuId;
	}

	public void setParentMenuId(String parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public Specification<Menu> toSpecification() {
		return new Specification<Menu>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Menu> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicate = new ArrayList<>();
				if (!StringUtils.isEmpty(getName())) {
					predicate.add(cb.like(root.get(Fields.NAME), "%" + getName() + "%"));
				}
				if (!StringUtils.isEmpty(getFunctionCode())) {
					predicate.add(cb.like(root.get(Fields.FUNCTION_CODE), "%" + getFunctionCode() + "%"));
				}
				if (!StringUtils.isEmpty(getParentMenuId())) {
					predicate.add(cb.equal(root.get(Fields.PARENT).get(Fields.ID), getParentMenuId()));
				}
				if (!StringUtils.isEmpty(getLevel())) {
					predicate.add(cb.equal(root.get(Fields.LEVEL), getLevel()));
				}

				// 对于指定了 parentId 则按照 sequence 排序.
				if (StringUtils.isEmpty(getParentMenuId())) {
					query.orderBy(cb.asc(root.get(Fields.ID)));
				}
				else {
					query.orderBy(cb.asc(root.get(Fields.SEQUENCE)));
				}

				query.where(predicate.toArray(new Predicate[predicate.size()]));
				return query.getRestriction();
			}
		};
	}
}
