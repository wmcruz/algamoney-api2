package com.example.algamoney.api.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.example.algamoney.api.config.property.AlgamoneyApiProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

/**
 * Classe component, responsavel por de fato salvar um arquivo temporariamente no aws S3.
 * @author Wellington
 */
@Component
public class S3 {

    private static final Logger LOGGER = LoggerFactory.getLogger(S3.class);

    @Autowired
    private AlgamoneyApiProperty property;

    @Autowired
    private AmazonS3 amazonS3;

    public String salvarTemporariamente(MultipartFile arquivo) {
        // atribuindo os grant, as permissoes de acesso ao arquivo/objeto
        AccessControlList accessControlList = new AccessControlList();
        accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read);

        // criacao do metadata
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(arquivo.getContentType());
        objectMetadata.setContentLength(arquivo.getSize());

        String nomeUnico = gerarNomeUnico(arquivo.getOriginalFilename()); // gerando um nome de arquivo unico

        try {
            // montando arquivo/objeto para ser inserido no aws s3
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    property.getS3().getBucket(),
                    nomeUnico,
                    arquivo.getInputStream(),
                    objectMetadata)
                    .withAccessControlList(accessControlList);

            putObjectRequest.setTagging(new ObjectTagging(Arrays.asList(new Tag("expirar", "true")))); // implementando a tag 'expirar' no objeto/arquivo

            amazonS3.putObject(putObjectRequest); // salvando o arquivo no aws s3

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Arquivo {} enviado com sucesso para o s3.",
                        arquivo.getOriginalFilename());
            }

            return nomeUnico;
        } catch (IOException e) {
            throw new RuntimeException("Problemas ao tentar enviar o arquivo para o S3.", e);
        }
    }

    public String configurarUrl(String objeto) {
        return "\\" + property.getS3().getBucket() + ".s3.amazonaws.com/" + objeto;
    }

    private String gerarNomeUnico(String originalFilename) {
        return UUID.randomUUID().toString() + "_" + originalFilename;
    }
}