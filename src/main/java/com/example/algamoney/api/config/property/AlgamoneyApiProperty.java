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

    // instancias de classes
    private final Seguranca seguranca = new Seguranca();

    private final Mail mail = new Mail();

    private final S3 s3 = new S3();

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

    public S3 getS3() { return s3; }

    // classes
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

    /**
     * Classe model para armazenar informações referente ao serviço da amazon s3
     * @author Wellington
     */
    public static class S3 {
        private String accessKeyId;
        private String secretAccessKey;
        private String bucket = "wmc-algamoney-arquivos";

        public String getAccessKeyId() {
            return accessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        public String getSecretAccessKey() {
            return secretAccessKey;
        }

        public void setSecretAccessKey(String secretAccessKey) {
            this.secretAccessKey = secretAccessKey;
        }

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
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