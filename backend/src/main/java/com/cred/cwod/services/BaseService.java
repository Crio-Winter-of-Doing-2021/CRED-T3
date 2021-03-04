package com.cred.cwod.services;

import org.springframework.http.ResponseEntity;

public interface BaseService {

  /**
   * Get the specific entity by it's object id
   *
   * @param id - object id of entity to be find.
   * @return entity if it is present
   */
  ResponseEntity<?> findEntityById(String id);

  /**
   * Change status of the specific id as Status.INACTIVE
   *
   * @param id - object id of the entity whose status needed to update
   * @return - updated entity as response
   */
  ResponseEntity<?> deleteEntity(String id);

  /**
   * Change status of the specific id as Status.ACTIVE
   *
   * @param id - object id of the entity whose status needed to update
   * @return - updated entity as response
   */
  ResponseEntity<?> restoreEntity(String id);
}
