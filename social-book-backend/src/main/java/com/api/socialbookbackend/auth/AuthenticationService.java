package com.api.socialbookbackend.auth;

import com.api.socialbookbackend.email.EmailService;
import com.api.socialbookbackend.email.EmailTemplateName;
import com.api.socialbookbackend.role.Role;
import com.api.socialbookbackend.role.RoleRepository;
import com.api.socialbookbackend.security.JwtService;
import com.api.socialbookbackend.user.Token;
import com.api.socialbookbackend.user.TokenRepository;
import com.api.socialbookbackend.user.User;
import com.api.socialbookbackend.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${application.security.jwt.token.length}")
    private int tokenLength;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    /**
     * Register a new user
     * @param registrationRequest
     */
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

    /**
     * Authenticate the user
     * @param authenticationRequest
     * @return
     */
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {

        //check if the user is enabled
        User findUser = userRepository.findByEmail(authenticationRequest.email())
                .orElseThrow(() -> new EntityNotFoundException("Error: User not found."));

        if(!findUser.isEnabled()){
            throw new DisabledException("Error: Account is not activated.");
        }
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.email(),
                        authenticationRequest.password()
                ));
        Map<String, Object> claims = new HashMap<>();
        User user = ((User) authenticate.getPrincipal());
        claims.put("fullName", user.getFullName());
        String token = jwtService.generateToken(claims, (User) authenticate.getPrincipal());
        //return the token
        return new AuthenticationResponse(token);
    }

    /**
     * Send an email verification to the user
     * @param user
     * @throws RuntimeException
     * @throws MessagingException
     */
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

    /**
     * Activate the user account
     * @param token
     * @throws MessagingException
     */
    public void activateAccount(String token) throws MessagingException {
        Token savedToken= tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Error: Token not found."));
        if(LocalDateTime.now().isAfter(savedToken.getExpiresAt())){
            sendEmailVerification(savedToken.getUser());
            throw new RuntimeException("Error: Token has expired. A new token is sent to your email.");
        }
        User user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }
}
