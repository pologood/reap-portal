package org.reap.portal.web.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.reap.portal.common.Fields;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * 拦截器：用于调用外部系统服务时，传递令牌
 * @author hehaiwei
 *
 */
public class TokenRequestInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		HttpServletRequest original = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		request.getHeaders().add(Fields.TOKEN, original.getHeader(Fields.TOKEN));
		return execution.execute(request, body);
	}

}
