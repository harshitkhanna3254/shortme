package com.comp519.shortme.services;

import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UrlShorteningService {

    @Autowired
    private BigtableDataClient bigtableDataClient;

    @Value("${bigtable.tables.urlmappings}")
    private String urlMappingsTable;

    @Value("${bigtable.tables.urlmappings.family}")
    private String columnFamily;

    public String saveUrlMapping(String shortLink, String longUrl) {

        System.out.println(urlMappingsTable);
        System.out.println(columnFamily);

        // Row key will be the short link
        String rowKey = shortLink;

        String timestamp = LocalDateTime.now().toString();

        RowMutation rowMutation = RowMutation.create(urlMappingsTable, rowKey)
                .setCell(columnFamily, ByteString.copyFromUtf8("longUrl"), System.currentTimeMillis() * 1000, ByteString.copyFromUtf8(longUrl))
                .setCell(columnFamily, ByteString.copyFromUtf8("createdAt"), System.currentTimeMillis() * 1000, ByteString.copyFromUtf8(timestamp));

        // Perform the mutation
        bigtableDataClient.mutateRow(rowMutation);
        System.out.println("Successfully saved URL mapping to BigTable.");

        return timestamp;
    }


    public String retrieveOriginalUrl(String shortCode) {
        // Implementation of URL retrieval

        return "Long URL";
    }
}
