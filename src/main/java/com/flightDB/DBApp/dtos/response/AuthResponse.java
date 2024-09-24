package com.flightDB.DBApp.dtos.response;
import com.flightDB.DBApp.model.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthResponse {
    private String token;
    private ERole role;
}
