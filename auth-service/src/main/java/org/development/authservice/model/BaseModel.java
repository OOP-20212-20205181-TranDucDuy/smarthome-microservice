package org.development.authservice.model;

import lombok.*;
import org.development.authservice.enums.RoleType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;


import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseModel {
    @Id
    private String id;

    @CreatedDate
    @Field
    private Date createdAt;
    @LastModifiedDate
    @Field
    private Date updatedAt;
    @Field
    private boolean isDelete = false;
    @Field
    private int userId;
    @Field
    private RoleType role;
}
