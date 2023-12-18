package com.kingtest.crypto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kingtest.entities.User;

@SpringBootTest
public class JwtServiceTest {
    User user  = User.builder()
        .email("email")
        .role("M")
        .id(1L)
        .username("name")
        .build();

    @Autowired
    JwtService jwtService;

    @Test
    void testGenerateJwtFromUser() throws Exception {
        
        String jwt = jwtService.makeBodyJwt(user);
        System.out.println(jwt);
    }
}
