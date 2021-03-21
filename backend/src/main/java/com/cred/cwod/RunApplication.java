package com.cred.cwod;

import com.cred.cwod.configs.StorageProperties;
import com.cred.cwod.dto.Media;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@Log4j2
@EnableConfigurationProperties({StorageProperties.class, Media.class})
public class RunApplication {

  public static void main(String[] args) {
    SpringApplication.run(RunApplication.class, args);
    log.info("Backend started!!");
  }

  /**
   * The most commonly used hashing algorithm is BCrypt
   * and it is a recommended method of secure hashing.
   * <p>
   * This will be used to hash the passwords and other sensitive information.
   *
   * @return BCryptEncoder
   */
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
