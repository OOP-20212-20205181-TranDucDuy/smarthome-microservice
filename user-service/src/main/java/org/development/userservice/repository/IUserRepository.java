package org.development.userservice.repository;

import org.development.userservice.model.UserModel;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface IUserRepository extends MongoRepository<UserModel , String> {
    Optional<UserModel> findUserModelByEmail(String email);
    @ExistsQuery("{'email':  ?0}")
    boolean existsUserModelsByEmail(String email);
    @Query("{'userId':  ?0}")
    Optional<UserModel> findUserModelByUserId(int userId);
}
