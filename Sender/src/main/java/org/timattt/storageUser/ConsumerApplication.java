package org.timattt.storageUser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"org.timattt.common"})
@SpringBootApplication(scanBasePackages = {"org.timattt.common", "org.timattt.storageUser"})
@EntityScan(basePackages = {"org.timattt.common"})
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}
