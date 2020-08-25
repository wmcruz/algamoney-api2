package com.example.algamoney.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Classe reponsavel por configurar propriedades para o sistema
 * Opções uteis para utilizar em ambiente locais e de produção
 *
 * @author welyn
 */
@ConfigurationProperties("algamoney")
public class AlgamoneyApiProperty {

    // Atributos
    private String origemPermitida = "http://localhost:4200";

    private final Seguranca seguranca = new Seguranca();

    private final Mail mail = new Mail();

    //Getters and setters
    public Seguranca getSeguranca() {
        return seguranca;
    }

    public Mail getMail() {
        return mail;
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

    public static class Mail {
        // Atributos
        private String host;
        private Integer port;
        private String username;
        private String password;

        // Getters and Setters
        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}