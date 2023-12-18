package com.kingtest.crypto;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kingtest.entities.User;

@Service
public class JwtService {
    private String kid;
    private String secretKey;
    private Key signatureKey;
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private ObjectMapper oMapper = new ObjectMapper();

    public JwtService(
            @Value("${spring.jwt.secretKey}") String secretKey,
            @Value("${spring.jwt.kid}") String kid) {
        this.secretKey = secretKey;
        this.kid = kid;
        this.signatureKey = loadKey();
    }

    private Key loadKey() {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        return signingKey;
    }

    public String makeBodyJwt(User user) {
        Map<String, Object> claims = oMapper.convertValue(user, new TypeReference<Map<String, Object>>() {
        });

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setSubject(user.getUsername())
                .setIssuer(user.getEmail())
                .setClaims(claims)
                .setHeaderParam("kid", kid)
                .setHeaderParam("typ", Header.JWT_TYPE)
                .signWith(signatureKey, signatureAlgorithm);

        // Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public String generateJwtFromUser(User user) throws Exception {
        byte[] userByes = convertUserToByteArray(user);
        String encodedUser = Base64.getEncoder().encodeToString(userByes);
        return encodedUser;
    }

    private byte[] convertUserToByteArray(User user) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(user);
        oos.flush();
        byte[] data = bos.toByteArray();
        return data;
    }

    public boolean doesJwtHasRequiredRole(String jwt, List<String> authorizedRoles) {
        Jws<Claims> jwts = Jwts.parserBuilder()
                .setSigningKey(signatureKey)
                .build()
                .parseClaimsJws(jwt);

        String clientRole = jwts.getBody().get("role").toString();
        return authorizedRoles.contains(clientRole);
    }
}
