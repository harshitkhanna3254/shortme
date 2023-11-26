package com.comp519.shortme.services;

import com.comp519.shortme.dto.AllUrlsResponseDto;
import com.comp519.shortme.dto.UrlResponseDto;
import com.comp519.shortme.dto.UserResponseDto;
import com.comp519.shortme.exceptions.UrlNotCreatedByUser;
import com.comp519.shortme.exceptions.UrlNotFoundException;
import com.comp519.shortme.utils.Utils;
import com.google.api.gax.rpc.NotFoundException;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.Filters;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static com.comp519.shortme.constants.ApplicationConstants.FORBIDDEN_MESSAGE;
import static com.comp519.shortme.constants.ApplicationConstants.NOT_FOUND_MESSAGE;
import static com.comp519.shortme.constants.BigTableConstants.*;

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

    private final BigtableDataClient bigtableDataClient;

    public UrlShorteningService(BigtableDataClient bigtableDataClient) {
        this.bigtableDataClient = bigtableDataClient;
    }

    public UrlResponseDto saveUrlMapping(String shortLink, String longUrl) {

        // For saving `created_at`
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String createdAt = now.format(formatter);

        RowMutation urlTableRowMutation = RowMutation.create(urlMappingsTable, shortLink)
                .setCell(urlDataColumnFamily, LONG_URL_QUALIFIER, System.currentTimeMillis() * 1000, longUrl)
                .setCell(urlDataColumnFamily, CREATED_AT_QUALIFIER, System.currentTimeMillis() * 1000, createdAt);

        String shortUrl = Utils.convertShortLinkToUrl(shortLink);

        UrlResponseDto urlResponseDto = new UrlResponseDto();
        urlResponseDto.setShortUrl(shortUrl);
        urlResponseDto.setLongUrl(longUrl);
        urlResponseDto.setCreatedAt(createdAt);

        // Check if the user is authenticated
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {

            String username = ((UserResponseDto) authentication.getPrincipal()).getUsername();
            urlTableRowMutation.setCell(userDataColumnFamily, USERNAME_QUALIFIER, System.currentTimeMillis() * 1000, username);
            urlResponseDto.setUsername(username);

            // Append the short url to the list of Urls in the User table
            List<String> urls = getUserUrls(username);
            urls.add(shortLink);
            String updatedUrls = String.join(",", urls);

            RowMutation userTableRowMutation = RowMutation.create(userTable, username)
                    .setCell(urlsColumnFamily, SHORT_URLS_QUALIFIER, System.currentTimeMillis() * 1000, updatedUrls);

            bigtableDataClient.mutateRow(userTableRowMutation);
        }

        bigtableDataClient.mutateRow(urlTableRowMutation);

        return urlResponseDto;
    }

    public UrlResponseDto retrieveOriginalUrl(String shortCode) throws NotFoundException, ExecutionException, InterruptedException {
        // Implementation of URL retrieval

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserResponseDto) authentication.getPrincipal()).getUsername();

        Row row = bigtableDataClient.readRowAsync(urlMappingsTable, ByteString.copyFromUtf8(shortCode)).get();

        if(row == null)
            throw new UrlNotFoundException(NOT_FOUND_MESSAGE);

        UrlResponseDto urlResponseDto = mapRowToUrlData(row);

        if(!urlResponseDto.getUsername().equals(username))
            throw new UrlNotCreatedByUser(FORBIDDEN_MESSAGE);

        return urlResponseDto;
    }

    public AllUrlsResponseDto retrieveAllUrlsOfUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserResponseDto) authentication.getPrincipal()).getUsername();

        List<String> urls = getUserUrls(username);
        return new AllUrlsResponseDto(urls);
    }

    /*
        Helper Functions
    */
    public List<String> getUserUrls(String username) {

        List<String> urls = new ArrayList<>();

        Filters.Filter filter = Filters.FILTERS.chain()
                .filter(Filters.FILTERS.family().exactMatch(urlsColumnFamily))
                .filter(Filters.FILTERS.qualifier().exactMatch(SHORT_URLS_QUALIFIER));

        Row row = bigtableDataClient.readRow(userTable, username, filter);

        if (row != null && !row.getCells(urlsColumnFamily, SHORT_URLS_QUALIFIER).isEmpty()) {
            String urlList = row.getCells(urlsColumnFamily, SHORT_URLS_QUALIFIER).get(0).getValue().toStringUtf8();
            urls.addAll(Arrays.asList(urlList.split(",")));
        }

        return Utils.convertShortLinksToUrls(urls);
    }

    private UrlResponseDto mapRowToUrlData(Row row) {
        UrlResponseDto urlResponseDto = new UrlResponseDto();
        urlResponseDto.setShortUrl(row.getKey().toStringUtf8());

        Map<String, Consumer<String>> mappingStrategies = new HashMap<>();
        mappingStrategies.put(LONG_URL_QUALIFIER, urlResponseDto::setLongUrl);
        mappingStrategies.put(CREATED_AT_QUALIFIER, urlResponseDto::setCreatedAt);
        mappingStrategies.put(USERNAME_QUALIFIER, urlResponseDto::setUsername);

        // Iterate over the cells in the row and apply the mapping strategies
        row.getCells().forEach(cell -> {
            String qualifier = cell.getQualifier().toStringUtf8();
            String value = cell.getValue().toStringUtf8();
            Consumer<String> strategy = mappingStrategies.get(qualifier);

            if (strategy != null) {
                strategy.accept(value);
            }
        });

        return urlResponseDto;
    }

}
