package com.cred.cwod.repository;

import com.cred.cwod.dto.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

  User findByEmailAndUsername(String email, String username);

  User findByUsername(String username);

  User findByUsernameAndAndPassword(String username, String password);

  User findByEmail(String email);
}