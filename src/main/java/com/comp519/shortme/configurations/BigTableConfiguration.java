package com.comp519.shortme.configurations;

import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;

@Configuration
public class BigTableConfiguration {

    @Value("${spring.cloud.gcp.bigtable.project-id}")
    private String projectId;

    @Value("${spring.cloud.gcp.bigtable.instance-id}")
    private String instanceId;

    @Bean
    public CommandLineRunner commandLineRunner() {
        System.out.println("Command Line Runner");
        return args -> {
            String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
            if (credentialsPath == null) {
                throw new IllegalStateException("GOOGLE_APPLICATION_CREDENTIALS environment variable is not set.");
            }
            System.out.println("GOOGLE_APPLICATION_CREDENTIALS is set to: " + credentialsPath);
            File credentialsFile = new File(credentialsPath);
            if (!credentialsFile.exists()) {
                throw new IllegalStateException("The credentials file does not exist at: " + credentialsPath);
            }
        };
    }

    @Bean
    public BigtableDataClient bigtableDataClient() throws IOException {
        System.out.println(projectId);
        System.out.println(instanceId);

        BigtableDataSettings settings = BigtableDataSettings.newBuilder()
                .setProjectId(projectId)
                .setInstanceId(instanceId)
                .build();

        return BigtableDataClient.create(settings);
    }
}

