package com.mphj.todo.utils;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class MailHelper {

    private static Mailer mailer;

    static {

        mailer = MailerBuilder
                .withSMTPServer("smtp.mailtrap.io", 2525, "cde4ae573bffb0", "ee6d5dfaa8cc1c")
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withSessionTimeout(10 * 1000)
                .clearEmailAddressCriteria()
                .withDebugLogging(true)
                .buildMailer();
    }

    public static void sendVerificationCode(String to, String verificationCode) {
        Email email = EmailBuilder.startingBlank()
                .to(to)
                .from("113cafe0d9-69da13@inbox.mailtrap.io")
                .withSubject("Todo - Email Verification")
                .withHTMLText(getFile("html/email_verification_code.html")
                        .replaceAll("#token#", verificationCode)
                        .replaceAll("#host#", "localhost:8080")
                )
                .withHeader("X-Priority", 5)
                .buildEmail();
        mailer.sendMail(email);
    }


    private static String getFile(String fileName) {

        StringBuilder result = new StringBuilder("");

        ClassLoader classLoader = MailHelper.class.getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();

    }
}
