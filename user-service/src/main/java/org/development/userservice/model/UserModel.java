package org.development.apigateway.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collation = "user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class UserModel {
    @Id
    private String id;
    @Field
    private String name;
}
