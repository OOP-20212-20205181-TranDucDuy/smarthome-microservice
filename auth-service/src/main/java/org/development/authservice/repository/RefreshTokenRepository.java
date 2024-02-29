package org.development.authservice.repository;

import org.development.authservice.model.RefreshTokenModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

public interface RefreshTokenRepository extends MongoRepository<RefreshTokenModel, String> {
    @Query(value = "{'userId': ?0}")
    @Update("{'$set': {'isDelete': true}}")
    void updateRefreshTokenModels(int userId);
}
