package com.example.algamoney.api.token;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // Notacao para dar prioridade alta a esta classe - ela é analisada primeiro dentro as requisicoes
public class RefreshTokenCookiePreProcessorFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request; // convertendo o request
		
		// regra de filtro apenas para requisicoes referente ao oauth2 - para pegar o refresh_token
		if("/oauth/token".equalsIgnoreCase(req.getRequestURI())
				&& "refresh_token".equals(req.getParameter("grant_type"))
				&& req.getCookies() != null) {
			
			// de todos os valores do cookie, o programa filtra apenas o refresh_token
			for (Cookie cookie : req.getCookies()) {
				if(cookie.getName().equals("refreshToken")) {					
					String refreshToken = cookie.getValue();
					req = new MyServletRequestWrapper(req, refreshToken);
				}
			}
		}
		
		chain.doFilter(req, response); // retornando a requisicao a pagina
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
	
	/**
	 * Classe interna feita apenas retornar a nova requisicao com o refresh_token aplicado
	 * @author Wellington
	 *
	 */
	static class MyServletRequestWrapper extends HttpServletRequestWrapper {
		//Atributos
		private String refreshToken;
		
		//Construtor
		public MyServletRequestWrapper(HttpServletRequest request, String refreshToken) {
			super(request);
			this.refreshToken = refreshToken;
		}
		
		 /**
		  * Criando um map que vai resgatar o novo refresh_token e devolver a requisicao
		  */
		@Override
		public Map<String, String[]> getParameterMap() {
			
			ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
			
			map.put("refresh_token", new String[] { refreshToken });
			map.setLocked(true);
			
			return map;
		}
	}
}