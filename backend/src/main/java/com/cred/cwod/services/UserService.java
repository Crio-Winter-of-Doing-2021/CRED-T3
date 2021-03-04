package com.cred.cwod.services;

import com.cred.cwod.dto.User;
import com.cred.cwod.exchanges.LoginRequest;
import com.cred.cwod.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements BaseService {

  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  @Autowired
  private UserRepository userRepository;

  public UserService(BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  public User createUser(User user) {
    if (isUserAlreadyExist(user)) {
      return null;
    }

    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    userRepository.save(user);
    return userRepository.findByEmailAndUsername(user.getEmail(), user.getUsername());
  }

  private boolean isUserAlreadyExist(User user) {
    return (userRepository.findByEmailAndUsername(user.getEmail(), user.getUsername()) != null);
  }

  /**
   * Get the specific entity by it's object id
   *
   * @param id - object id of entity to be find.
   * @return entity if it is present
   */
  @Override
  public ResponseEntity<?> findEntityById(String id) {
    return null;
  }

  /**
   * Change status of the specific id as Status.INACTIVE
   *
   * @param id - object id of the entity whose status needed to update
   * @return - updated entity as response
   */
  @Override
  public ResponseEntity<?> deleteEntity(String id) {
    userRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

  /**
   * Change status of the specific id as Status.ACTIVE
   *
   * @param id - object id of the entity whose status needed to update
   * @return - updated entity as response
   */
  @Override
  public ResponseEntity<?> restoreEntity(String id) {
    return null;
  }


  public User validateUser(LoginRequest loginRequest) {
    loginRequest.setPassword(bCryptPasswordEncoder.encode(loginRequest.getPassword()));
    return userRepository.findByUsernameAndAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
  }

  public User findUserId(String username) {
    return userRepository.findByUsername(username);
  }
}
