package org.development.apigateway.repository;

import org.development.apigateway.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IUserRepository extends MongoRepository<UserModel , String> {
}
