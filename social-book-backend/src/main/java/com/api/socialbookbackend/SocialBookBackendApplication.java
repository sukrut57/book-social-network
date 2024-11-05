package com.api.socialbookbackend;

import com.api.socialbookbackend.role.Role;
import com.api.socialbookbackend.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SocialBookBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialBookBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(RoleRepository roleRepository) {
        return args -> {
           if(roleRepository.findByName("USER").isEmpty()){
               roleRepository.save(
                       Role.builder().name("USER").build());
           }
        };
    }

}
