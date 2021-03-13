package com.cred.cwod.repositoryServices;

import com.cred.cwod.dto.Media;
import com.cred.cwod.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MediaRepositoryService {

  @Autowired
  private MediaRepository mediaRepository;

  public Media findMediaByCardId(String userId) {
    List<Media> mediaList = mediaRepository.findAll();

    for (Media media : mediaList) {
      if (media.getCardId().equalsIgnoreCase(userId))
        return media;
    }

    return null;
  }

  public void save(Media mediaToSave) {
    mediaRepository.save(mediaToSave);
  }

  public String getUploadMediaPath(String cardId, String docType) {
    return cardId + "_" + docType + "." + docType;
  }
}

