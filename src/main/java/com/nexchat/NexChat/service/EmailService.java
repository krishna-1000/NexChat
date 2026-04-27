package com.nexchat.NexChat.service;

import com.nexchat.NexChat.exception.DuplicateEntryException;
import com.nexchat.NexChat.exception.ExternalServiceException;
import com.nexchat.NexChat.exception.InvalidActionException;
import com.nexchat.NexChat.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final Map<String, String> otpStore = new HashMap<>();
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final UserRepository userRepository;

    public String generateOtp() {
        SecureRandom sr = new SecureRandom();
        int number = sr.nextInt(1000000);
        return String.format("%06d", number);
    }

    public void sendOtpToEmail(String toMail) {
        final String generatedOtp = generateOtp();
        String otpData = generatedOtp + System.currentTimeMillis();
        try {
            Context context = new Context();
            context.setVariable("otp", generatedOtp);
            String process = templateEngine.process("otp-mail", context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom("krishnpalpatidar17@gmail.com", "NextChat team");
            helper.setTo(toMail);
            helper.setReplyTo("no-reply@gmail.com");
            helper.setSubject("NexChat - Your Verification Code");
            helper.setText(process, true);
            otpStore.put(toMail, otpData);
            mailSender.send(mimeMessage);



        } catch (RuntimeException | UnsupportedEncodingException | MessagingException e) {
            otpStore.remove(toMail);
            throw new ExternalServiceException("We couldn't reach the mail server. Please try again.");
        }


    }

    public String verifyOtp(String mail, String otp) {

        String storedOtp = otpStore.get(mail);
        if (storedOtp == null) {
            throw new InvalidActionException("Verification code expired or not found. Please resend.");

        }

        String extractedOtp = storedOtp.substring(0, 6);

        if (!extractedOtp.equals(otp)) {
            throw new InvalidActionException("Invalid varification code");
        }
        try {
            long otpGenerationTime = Long.parseLong(storedOtp.substring(6));
            long fiveMinutes = (5 * 60 * 1000);
            if (System.currentTimeMillis() - otpGenerationTime > fiveMinutes) {
                otpStore.remove(mail);
                throw new InvalidActionException("varification code is expired please resend!");

            }
            otpStore.remove(mail);
            boolean isEmailExist = userRepository.existsByEmail(mail);
            if(isEmailExist){
               throw  new DuplicateEntryException("This email is already registered");
            }
            return "Varification successful!";
        } catch (NumberFormatException e) {
            throw new ExternalServiceException("Internal error processing verification code.");
        }

    }
}
