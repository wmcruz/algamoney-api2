package com.example.algamoney.api.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.algamoney.api.config.property.AlgamoneyApiProperty;

@ControllerAdvice
/**
 * Classe responsavel por retirar o refresh_token do corpo da requisicao no /oauth/token
 * e criar um token
 * @author Wellington
 *
 */
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken>{
	
	@Autowired
	private AlgamoneyApiProperty algamoneyApiProperty;

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return returnType.getMethod().getName().equals("postAccessToken");
	}

	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body;
		
		String refreshToken = body.getRefreshToken().getValue(); // resgatando o valor do refresh_token
		
		// convertendo o request e o response
		HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
		HttpServletResponse resp = ((ServletServerHttpResponse) response).getServletResponse();
		
		adicionarRefreshTokenNoCookie(refreshToken, req, resp); // criando o cookie
		removerRefreshTokenDoBody(token); // removendo o refresh_token da requisicao
		
		return body;
	}
	
	/**
	 * Metodo criado para remover o refresh_token da requisicao 
	 * @param token
	 */
	private void removerRefreshTokenDoBody(DefaultOAuth2AccessToken token) {
		token.setRefreshToken(null);
	}

	/**
	 * Atraves de uma requisicao repassada por parametro, o metodo cria um cookie
	 * @param refreshToken
	 * @param req
	 * @param resp
	 */
	private void adicionarRefreshTokenNoCookie(String refreshToken, HttpServletRequest req, HttpServletResponse resp) {
		Cookie refreshCookieToken = new Cookie("refreshToken", refreshToken);
		refreshCookieToken.setHttpOnly(true);
		refreshCookieToken.setSecure(algamoneyApiProperty.getSeguranca().isEnableHttps());
		refreshCookieToken.setPath(req.getContextPath() + "/oauth/token");
		refreshCookieToken.setMaxAge(2592000);
		resp.addCookie(refreshCookieToken);
	}
}