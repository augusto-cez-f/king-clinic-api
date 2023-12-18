package com.kingtest.dto.responses.jwtResponses;

import java.util.List;

import com.kingtest.services.UserDataService.UserData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
  public final static String BEARER_TYPE = "Bearer";

  private String token;
  private String type;
  private Long id;
  private String username;
  private String email;
  private List<String> roles;
  private UserData userData;

  public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles) {
    this.token = accessToken;
    this.id = id;
    this.username = username;
    this.email = email;
    this.roles = roles;
  }
}
