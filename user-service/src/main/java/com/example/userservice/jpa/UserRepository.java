package com.example.userservice.jpa;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    //jpql 오..감격..
    UserEntity findByUserId(String userId);

    UserEntity findByEmail(String username);
}
