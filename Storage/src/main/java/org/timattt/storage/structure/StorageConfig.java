package org.timattt.storage.structure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Bean
    public SerializationBasedKeyValueStorage<?, ?> storage(@Value("${storage.location}") String location) {
        return new SerializationBasedKeyValueStorage<String, String>(location);
    }

}
