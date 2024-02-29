package org.development.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.development.userservice.enums.RoleType;

@Data
@Builder
@AllArgsConstructor
public class UserVO {
    private int id;
    private String email;
    private String password;
    private RoleType role;
}
