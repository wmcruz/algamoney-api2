package com.example.algamoney.api.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;

@Component
public class Mailer {

    @Autowired
    private JavaMailSender mailSender;

    /* Criação de um evento para disparar um e-mail no inicio da aplicação.
     o mesmo método pode ser utilizado sempre que quiséssemos enviar um e-mail */
    // @EventListener
    // private void teste(ApplicationReadyEvent event) {
    //    this.enviarEmail("wellington.max1@gmail.com", Arrays.asList("welyngton_lk@hotmail.com"), "Testando", "Olá!<br />Teste ok.");
    //    System.out.println("Terminado o envio de e-mail....");
    //}

    public void enviarEmail(String remetente,
                            List<String> destinatarios, String assunto, String mensagem) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setFrom(remetente);
            helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
            helper.setSubject(assunto);
            helper.setText(mensagem, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Problemas com o envio de e-mail!", e);
        }
    }
}