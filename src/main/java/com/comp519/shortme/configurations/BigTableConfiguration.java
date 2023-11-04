package com.comp519.shortme.configurations;

//import com.google.cloud.bigtable.data.v2.BigtableDataClient;
//import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class BigTableConfiguration {

//    @Value("${bigtable.projectId}")
//    private String projectId;
//
//    @Value("${bigtable.instanceId}")
//    private String instanceId;

//    @Bean
//    public BigtableDataClient bigtableDataClient() throws IOException {
//        BigtableDataSettings settings = BigtableDataSettings.newBuilder()
//                .setProjectId(projectId)
//                .setInstanceId(instanceId)
//                .build();
//
//        return BigtableDataClient.create(settings);
//    }
}
