package com.cred.cwod.repository;

import com.cred.cwod.dto.Media;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MediaRepository extends MongoRepository<Media, String> {

}

