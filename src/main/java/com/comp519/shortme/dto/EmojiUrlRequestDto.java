package com.comp519.shortme.dto;

import com.comp519.shortme.validators.ValidUrl;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import static com.comp519.shortme.constants.ApplicationConstants.INVALID_URL_MESSAGE;
import static com.comp519.shortme.constants.ApplicationConstants.NOT_BLANK_MESSAGE;

@Data
public class EmojiUrlRequestDto {

    @NotBlank(message = NOT_BLANK_MESSAGE)
    @ValidUrl(message = INVALID_URL_MESSAGE)
    private String url;

    private String emoji1;
    private String emoji2;
}
