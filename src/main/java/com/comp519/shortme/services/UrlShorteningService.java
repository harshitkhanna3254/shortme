package com.comp519.shortme.services;

import com.comp519.shortme.constants.BigTableConstants;
import com.comp519.shortme.dto.UserResponseDto;
import com.google.api.gax.rpc.NotFoundException;
import com.google.api.gax.rpc.ServerStream;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.Filters;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UrlShorteningService {

    @Value("${bigtable.tables.names.urlTable}")
    private String urlMappingsTable;

    @Value("${bigtable.tables.names.userTable}")
    private String userTable;

    @Value("${bigtable.tables.urlTable.columnfamily.names.urldata}")
    private String urlDataColumnFamily;

    @Value("${bigtable.tables.urlTable.columnfamily.names.userdata}")
    private String userDataColumnFamily;

    @Value("${bigtable.tables.userTable.columnfamily.names.urls}")
    private String urlsColumnFamily;

    private final String longUrlQualifier = BigTableConstants.LONG_URL_QUALIFIER;
    private final String createdAtQualifier = BigTableConstants.CREATED_AT_QUALIFIER;
    private final String usernameQualifier = BigTableConstants.USERNAME_QUALIFIER;
    private final String shortUrlsQualifier = BigTableConstants.SHORT_URLS_QUALIFIER;

    private final BigtableDataClient bigtableDataClient;

    public UrlShorteningService(BigtableDataClient bigtableDataClient) {
        this.bigtableDataClient = bigtableDataClient;
    }

    public String saveUrlMapping(String shortLink, String longUrl) throws Exception{

        // For saving `created_at`
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String createdAt = now.format(formatter);

        RowMutation urlTableRowMutation = RowMutation.create(urlMappingsTable, shortLink)
                .setCell(urlDataColumnFamily, longUrlQualifier, System.currentTimeMillis() * 1000, longUrl)
                .setCell(urlDataColumnFamily, createdAtQualifier, System.currentTimeMillis() * 1000, createdAt);

        // Check if the user is authenticated
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            // Assuming the principal is your UserResponseDto with a getUsername method
            String username = ((UserResponseDto) authentication.getPrincipal()).getUsername();
            urlTableRowMutation.setCell(userDataColumnFamily, usernameQualifier, System.currentTimeMillis() * 1000, username);

            // Append the short url to the list of Urls in the User table
            List<String> urls = getUserUrls(username);
            urls.add(shortLink);
            String updatedUrls = String.join(",", urls);

            RowMutation userTableRowMutation = RowMutation.create(userTable, username)
                    .setCell(urlsColumnFamily, shortUrlsQualifier, System.currentTimeMillis() * 1000, updatedUrls);

            bigtableDataClient.mutateRow(userTableRowMutation);
        }

        bigtableDataClient.mutateRow(urlTableRowMutation);

        return createdAt;
    }

    public List<String> getUserUrls(String username) throws Exception {

        List<String> urls = new ArrayList<>();

        Filters.Filter filter = Filters.FILTERS.chain()
                .filter(Filters.FILTERS.family().exactMatch(urlsColumnFamily))
                .filter(Filters.FILTERS.qualifier().exactMatch(shortUrlsQualifier));

        Row row = bigtableDataClient.readRow(userTable, username, filter);

        if (row != null && !row.getCells(urlsColumnFamily, shortUrlsQualifier).isEmpty()) {
            String urlList = row.getCells(urlsColumnFamily, shortUrlsQualifier).get(0).getValue().toStringUtf8();
            urls.addAll(Arrays.asList(urlList.split(",")));
        }

        return urls;
    }


    public String retrieveOriginalUrl(String shortCode) throws NotFoundException {
        // Implementation of URL retrieval



        return "Long URL";
    }
}
