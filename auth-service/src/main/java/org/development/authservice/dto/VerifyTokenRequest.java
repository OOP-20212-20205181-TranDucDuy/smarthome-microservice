package org.development.authservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VerifyTokenRequest {
    private String token;
}
