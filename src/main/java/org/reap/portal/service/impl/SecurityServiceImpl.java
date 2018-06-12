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

package org.reap.portal.service.impl;

import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.reap.CoreException;
import org.reap.portal.common.ErrorCodes;
import org.reap.portal.service.SecurityService;
import org.reap.portal.vo.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author hehaiwei
 * @since 1.0
 */
@Component
public class SecurityServiceImpl implements SecurityService {

	@Value ("${token.key}")
	private String key;
	
	@Value ("${session.timeout:1200000}")
	private Long sessionTimeOut;
	
	private SecretKey secretKey = null;

	@Override
	public String generateToken(User user) {
		return generateToken(user.getId() + user.getName() + user.getUsername());
	}
	
	@Override
	public String parseToken(String token) {
		Claims claims = null;
		secretKey = secretKey == null ? new SecretKeySpec(Base64.decodeBase64(key), "AES") : secretKey;
		try {
			claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) {
			throw new CoreException(ErrorCodes.TOKEN_EXPIRED);
		} catch (MalformedJwtException e) {
			throw new CoreException(ErrorCodes.TOKEN_ERROR);
		}
		if (claims == null ) {
			throw new CoreException(ErrorCodes.TOKEN_ERROR);
		}
		String jwtSubject = claims.getSubject();
		if( System.currentTimeMillis() - claims.getIssuedAt().getTime() > sessionTimeOut * 0.8) {
			return generateToken(jwtSubject);
		}
		return token;
	}
	
	private String generateToken(String subject) {
		secretKey = secretKey == null ? new SecretKeySpec(Base64.decodeBase64(key), "AES") : secretKey;
		Date currentDate = new Date();
		return Jwts.builder().setIssuedAt(currentDate).setExpiration(new Date(currentDate.getTime() + sessionTimeOut)).setSubject(subject).signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}

	public void setKey(String key) {
		this.key = key;
	}

}
