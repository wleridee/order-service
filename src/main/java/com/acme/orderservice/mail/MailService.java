package com.acme.orderservice.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Envoi de mails transactionnels (simulé en local : simple log).
 */
@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    public void send(String to, String subject, String body) {
        log.info("Mail envoyé - sujet : {}", subject);
    }
}
