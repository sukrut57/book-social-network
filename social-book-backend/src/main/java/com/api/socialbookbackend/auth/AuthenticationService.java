package com.api.socialbookbackend.auth;

import com.api.socialbookbackend.email.EmailService;
import com.api.socialbookbackend.email.EmailTemplateName;
import com.api.socialbookbackend.role.Role;
import com.api.socialbookbackend.role.RoleRepository;
import com.api.socialbookbackend.user.Token;
import com.api.socialbookbackend.user.TokenRepository;
import com.api.socialbookbackend.user.User;
import com.api.socialbookbackend.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${application.security.jwt.token.length}")
    private int tokenLength;

    @Value("${application.security.jwt.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(@Valid RegistrationRequest registrationRequest) {
        //assign a user role to the user
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        //build user entity
        User newuser = User.builder()
                .firstName(registrationRequest.firstName())
                .lastName(registrationRequest.lastName())
                .email(registrationRequest.email())
                .dateOfBirth(LocalDate.parse(registrationRequest.dateOfBirth()))
                .password(passwordEncoder.encode(registrationRequest.password()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();

        try{
            //save user to the database
            userRepository.save(newuser);

            //send a verification email
            sendEmailVerification(newuser);
        }
        catch (MessagingException e){
            throw new RuntimeException("Error: unable to send email verification.");
        }
        catch (Exception e){
            throw new RuntimeException("Error: Unable to save the user, email is already in use.");
        }


    }

    private void sendEmailVerification(User user) throws RuntimeException, MessagingException {
        String newToken = generateAndSaveActivationToken(user);

        //send a verification email
        emailService.sendEmailVerification(
                user.getEmail(),
                user.getFirstName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Activate your account"
        );

    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationToken(tokenLength);
        Token token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        try{
            tokenRepository.save(token);
            return generatedToken;
        }
        catch (Exception e){
            throw new RuntimeException("Error: unable to save token.");
        }
    }

    private String generateActivationToken(int tokenLength) {
        String characters = "0123456789";
        StringBuilder tokenBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for(int i=0;i<tokenLength;i++){
            int randomIndex = secureRandom.nextInt(characters.length());
            tokenBuilder.append(characters.charAt(randomIndex));
        }
        return tokenBuilder.toString();
    }
}
