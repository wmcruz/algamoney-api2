package com.example.algamoney.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("algamoney")
/**
 * Classe reponsavel por configurar propriedades para o sistema
 * Opções uteis para utilizar em ambiente locais e de produção
 * 
 * @author welyn
 */
public class AlgamoneyApiProperty {
	
	// Atributos
	private String origemPermitida = "http://localhost:8000";
	
	private final Seguranca seguranca = new Seguranca();
	
	//Getters and setters
	public Seguranca getSeguranca() {
		return seguranca;
	}
	
	public String getOrigemPermitida() {
		return origemPermitida;
	}
	
	public void setOrigemPermitida(String origemPermitida) {
		this.origemPermitida = origemPermitida;
	}

	/**
	 * Classe responsavel por acoplar opções de segurança
	 * 
	 * @author welyn
	 */
	public static class Seguranca {
		
		private boolean enableHttps;

		public boolean isEnableHttps() {
			return enableHttps;
		}

		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}
	}
}