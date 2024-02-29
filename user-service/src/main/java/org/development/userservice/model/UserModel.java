package org.development.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;
import org.development.userservice.enums.RoleType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Transient;

@Document(collation = "en_US" ,collection = "user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserModel {
    @Id
    private String id;
    @Field
    private int userId;
    @Field
    @Indexed(unique = true,collation = "en_US")
    private String email;
    @Field
    private String password;
    @Field
    private RoleType role;
}
