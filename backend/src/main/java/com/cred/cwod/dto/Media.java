package com.cred.cwod.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "file")
public class Media {

  private String cardId;

  private String fileName;

  private String fileFormat;

  private String uploadDir;
}
