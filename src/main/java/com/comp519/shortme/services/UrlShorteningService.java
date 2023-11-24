package com.comp519.shortme.services;

import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UrlShorteningService {

    @Value("${bigtable.tables.urlmappings}")
    private String urlMappingsTable;

    @Value("${bigtable.tables.urlmappings.family}")
    private String columnFamily;

    private final BigtableDataClient bigtableDataClient;

    public UrlShorteningService(BigtableDataClient bigtableDataClient) {
        this.bigtableDataClient = bigtableDataClient;
    }

    public String saveUrlMapping(String shortLink, String longUrl) {

        String timestamp = LocalDateTime.now().toString();

        RowMutation rowMutation = RowMutation.create(urlMappingsTable, shortLink)
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
