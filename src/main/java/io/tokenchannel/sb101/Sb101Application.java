package io.tokenchannel.sb101;

import io.tokenchannel.*;
import io.tokenchannel.exceptions.InvalidCodeException;
import io.tokenchannel.exceptions.InvalidIdentifierException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@Slf4j
@SpringBootApplication
public class Sb101Application {

    public static void main(String[] args) {
        SpringApplication.run(Sb101Application.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(TokenChannel tokenChannel) {
        return (args) -> {

            System.out.print("Dime tu número de teléfono(E164): ");
            String identifier = new Scanner(System.in).nextLine();

            try {
                ChallengeResponse challengeResponse = tokenChannel.challenge(ChannelType.VOICE, identifier, ChallengeOptions.builder()
                        .language("es").charset(CodeCharSet.DEC).codeLength(4)
                        .build());


                System.out.print("Confirme el código que ha recibido: ");
                String validationCode = new Scanner(System.in).nextLine();
                try {
                    tokenChannel.authenticate(challengeResponse.getRequestId(), validationCode);
                    log.info("Challenge superado");
                } catch (InvalidCodeException e) {
                  log.error("Código Inválid. Inténtelo de nuevo");
                }

            } catch (InvalidIdentifierException e) {
                log.error("Identificador Inválido");
            }

        };
    }
}
