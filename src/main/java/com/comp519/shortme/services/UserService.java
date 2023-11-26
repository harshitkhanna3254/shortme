package com.comp519.shortme.services;

import com.comp519.shortme.constants.ApplicationConstants;
import com.comp519.shortme.constants.BigTableConstants;
import com.comp519.shortme.dto.UserLoginRequestDto;
import com.comp519.shortme.dto.UserRegisterRequestDto;
import com.comp519.shortme.dto.UserResponseDto;
import com.comp519.shortme.exceptions.InvalidCredentialsException;
import com.comp519.shortme.exceptions.UserAlreadyExistsException;
import com.comp519.shortme.exceptions.UserNotFoundException;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;


@Service
public class UserService {

    private final BigtableDataClient bigtableDataClient;
    private final PasswordEncoder passwordEncoder;

    @Value("${bigtable.tables.names.userTable}")
    private String userTable;

    @Value("${bigtable.tables.userTable.columnfamily.names.accountinfo}")
    private String accountInfoColumnFamily;

    String passwordQualifier = BigTableConstants.PASSWORD_QUALIFIER;
    String firstNameQualifier = BigTableConstants.FIRST_NAME_QUALIFIER;
    String lastNameQualifier = BigTableConstants.LAST_NAME_QUALIFIER;
    String createdAtQualifier = BigTableConstants.CREATED_AT_QUALIFIER;


    public UserService(BigtableDataClient bigtableDataClient, PasswordEncoder passwordEncoder) {
        this.bigtableDataClient = bigtableDataClient;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDto registerUser(UserRegisterRequestDto userRegisterRequestDto) throws Exception  {

        String username = userRegisterRequestDto.getUsername();

        // Check if row exists
        Row row = bigtableDataClient.readRowAsync(userTable, ByteString.copyFromUtf8(username)).get();

        if(row != null)
            throw new UserAlreadyExistsException(ApplicationConstants.USER_ALREADY_EXISTS_MESSAGE);

        String hashedPassword = passwordEncoder.encode(userRegisterRequestDto.getPassword());

        // For saving `created_at`
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String createdAt = now.format(formatter);

        RowMutation mutation = RowMutation.create(userTable, username)
                .setCell(accountInfoColumnFamily, passwordQualifier, System.currentTimeMillis() * 1000, hashedPassword)
                .setCell(accountInfoColumnFamily, firstNameQualifier, System.currentTimeMillis() * 1000, userRegisterRequestDto.getFirstName())
                .setCell(accountInfoColumnFamily, lastNameQualifier, System.currentTimeMillis() * 1000, userRegisterRequestDto.getLastName())
                .setCell(accountInfoColumnFamily, createdAtQualifier, System.currentTimeMillis() * 1000, createdAt);

        bigtableDataClient.mutateRow(mutation);

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUsername(username);
        userResponseDto.setFirstName(userRegisterRequestDto.getFirstName());
        userResponseDto.setLastName(userRegisterRequestDto.getLastName());
        userResponseDto.setCreatedAt(createdAt);

        // Return UserResponseDto
        return userResponseDto;
    }


    public UserResponseDto loginUser(UserLoginRequestDto userLoginRequestDto) throws UserNotFoundException, ExecutionException, InterruptedException {

        String username = userLoginRequestDto.getUsername();
        String password = userLoginRequestDto.getPassword();

        Row userRow = getUserByUsernameAndPassword(username, password);

        return mapRowToUser(userRow);
    }

    public Row getUserByUsernameAndPassword(String username, String password) throws ExecutionException, InterruptedException {

        Row row = bigtableDataClient.readRowAsync(userTable, ByteString.copyFromUtf8(username)).get();

        if(row == null)
            throw new UserNotFoundException(ApplicationConstants.NOT_FOUND_MESSAGE);

        String hashedPassword = row.getCells(accountInfoColumnFamily, passwordQualifier).stream()
                .findFirst()
                .map(cell -> cell.getValue().toStringUtf8())
                .orElse("");

        if(!passwordEncoder.matches(password, hashedPassword))
            throw new InvalidCredentialsException(ApplicationConstants.INVALID_LOGIN_CREDENTIALS_MESSAGE);

        return row;
    }

    private UserResponseDto mapRowToUser(Row row) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUsername(row.getKey().toStringUtf8()); // Assuming username is the row key

        Map<String, Consumer<String>> mappingStrategies = new HashMap<>();
        mappingStrategies.put(firstNameQualifier, userResponseDto::setFirstName);
        mappingStrategies.put(lastNameQualifier, userResponseDto::setLastName);
        mappingStrategies.put(createdAtQualifier, userResponseDto::setCreatedAt);

        // Iterate over the cells in the row and apply the mapping strategies
        row.getCells().forEach(cell -> {
            String qualifier = cell.getQualifier().toStringUtf8();
            String value = cell.getValue().toStringUtf8();
            Consumer<String> strategy = mappingStrategies.get(qualifier);

            if (strategy != null) {
                strategy.accept(value);
            }
        });

        return userResponseDto;
    }

//    private void printRowDetails(Row row) {
//        System.out.println("Row Key: " + row.getKey().toStringUtf8());
//        row.getCells().forEach(cell -> {
//            String family = cell.getFamily();
//            String qualifier = cell.getQualifier().toStringUtf8();
//            String value = cell.getValue().toStringUtf8();
//
//            System.out.printf("Family: %s, Qualifier: %s, Value: %s%n", family, qualifier, value);
//        });
//    }
}
