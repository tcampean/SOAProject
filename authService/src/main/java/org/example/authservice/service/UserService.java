package org.example.authservice.service;

import io.jsonwebtoken.Jwts;
import org.example.authservice.client.RabbitClient;
import org.example.authservice.data.UserEntity;
import org.example.authservice.exception.AccountExistsException;
import org.example.authservice.exception.InvalidLoginException;
import org.example.authservice.model.JwkSetResponseDTO;
import org.example.authservice.model.JwtResponseDTO;
import org.example.authservice.model.RegisterUserDTO;
import org.example.authservice.model.RegistrationEmailDTO;
import org.example.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;

    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;

    @Value("${jwt.expiration}")
    private long expiration;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RabbitClient rabbitClient;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RabbitClient rabbitClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.rabbitClient = rabbitClient;
    }

    public JwtResponseDTO authenticate(String email, String password) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidLoginException("User not exist"));
        if (passwordEncoder.matches(password, user.getPassword())) {
            return new JwtResponseDTO(generateToken(user.getUsername()));
        }
        throw new InvalidLoginException("Invalid password");
    }

    public JwkSetResponseDTO getJwks() {
        JwkSetResponseDTO.JwkResponseDTO responseDTO = JwkSetResponseDTO.JwkResponseDTO.builder()
                .kty(publicKey.getAlgorithm())
                .n(Base64.getUrlEncoder().encodeToString(publicKey.getModulus().toByteArray()))
                .e(Base64.getUrlEncoder().encodeToString(publicKey.getPublicExponent().toByteArray()))
                .alg("RS512")
                .use("sig")
                .build();
        return new JwkSetResponseDTO(List.of(responseDTO));
    }

    public JwtResponseDTO register(RegisterUserDTO registerUserDTO) {
        if (userRepository.existsByEmail(registerUserDTO.getEmail())) {
            throw new AccountExistsException("Email in use");
        }
        if (userRepository.existsByUsername(registerUserDTO.getUsername())) {
            throw new AccountExistsException("Username in use");
        }
        UserEntity user = mapFromDto(registerUserDTO);
        userRepository.save(user);
        rabbitClient.sendMessage(new RegistrationEmailDTO(user.getEmail(), user.getUsername()));
        return new JwtResponseDTO(generateToken(user.getUsername()));
    }

    private String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuer("http://localhost:4000")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(privateKey, Jwts.SIG.RS512)
                .compact();
    }

    private UserEntity mapFromDto(RegisterUserDTO registerUserDTO) {
        UserEntity user = new UserEntity();
        user.setUsername(registerUserDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
        user.setEmail(registerUserDTO.getEmail());
        return user;
    }
}
