package org.development.authservice.repository;

import org.development.authservice.model.AccessTokenModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.Optional;


public interface AccessTokenReposity extends MongoRepository<AccessTokenModel, String> {

    @Query(value = "{'userId': ?0}")
    @Update("{'$set': {'isDelete': true}}")
    void updateAccessTokenModels(int userId);

    @Query(value = "{'userId': ?0,'isDelete': true}")
    Optional<AccessTokenModel> findAccessTokenModelByUserId(int userId);

    @Query(value = "{'accessToken': ?0,'isDelete': false}")
    Optional<AccessTokenModel> findAccessTokenModelByAccessToken(String accessToken);
}
