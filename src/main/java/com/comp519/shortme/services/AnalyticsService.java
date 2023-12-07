package com.comp519.shortme.services;

import com.google.bigtable.v2.Cell;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.Range;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowCell;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.List;

import static com.comp519.shortme.constants.BigTableConstants.CLICK_COUNT_QUALIFIER;
import static com.comp519.shortme.constants.BigTableConstants.TIMESTAMPS_QUALIFIER;

@Service
public class AnalyticsService {

    @Value("${bigtable.tables.names.urlTable}")
    private String urlMappingsTable;

    @Value("${bigtable.tables.urlTable.columnfamily.names.analyticsdata}")
    private String analyticsDataColumnFamily;

    private final BigtableDataClient bigtableDataClient;

    public AnalyticsService(BigtableDataClient bigtableDataClient) {
        this.bigtableDataClient = bigtableDataClient;
    }

    public void updateAnalytics(String shortLink) {
        // Get the current timestamp
        Instant clickTimestamp = Instant.now();

        // Read the current click count and timestamp data
        Row row = bigtableDataClient.readRow(urlMappingsTable, shortLink);
        long currentClickCount = 0;
        String currentTimestamps = "";

        ByteString clickCountByteString = row.getCells(analyticsDataColumnFamily, CLICK_COUNT_QUALIFIER).get(0).getValue();
        currentClickCount = Long.parseLong(clickCountByteString.toStringUtf8());

        ByteString timestampsByteString = row.getCells(analyticsDataColumnFamily, TIMESTAMPS_QUALIFIER).get(0).getValue();
        currentTimestamps = timestampsByteString.toStringUtf8();

        // Increment the click count
        currentClickCount++;

        // Append the new timestamp to the existing string
        if (!currentTimestamps.isEmpty()) {
            currentTimestamps += ", ";
        }
        currentTimestamps += clickTimestamp.toString();

        long mutationTimestampMicros = System.currentTimeMillis() * 1000;

        // Prepare the row mutation for both updated click count and timestamp
        RowMutation rowMutation = RowMutation.create(urlMappingsTable, shortLink)
                .deleteCells(analyticsDataColumnFamily, ByteString.copyFromUtf8(CLICK_COUNT_QUALIFIER), Range.TimestampRange.unbounded())
                .deleteCells(analyticsDataColumnFamily, ByteString.copyFromUtf8(TIMESTAMPS_QUALIFIER), Range.TimestampRange.unbounded())
                .setCell(analyticsDataColumnFamily, CLICK_COUNT_QUALIFIER, mutationTimestampMicros, Long.toString(currentClickCount)) // Update click count
                .setCell(analyticsDataColumnFamily, TIMESTAMPS_QUALIFIER, mutationTimestampMicros, currentTimestamps); // Update timestamps

        // Execute the row mutation
        bigtableDataClient.mutateRow(rowMutation);
    }

}
