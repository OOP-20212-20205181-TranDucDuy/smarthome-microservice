package org.development.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.development.authservice.enums.RoleType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfo {
    private String email;
    private String role;
}
