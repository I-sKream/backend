package com.v1.iskream.layer.domain.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ThumbnailResponseDto {
    private String imgUrl;

    public ThumbnailResponseDto(String url) {
        this.imgUrl = url;
    }
}
