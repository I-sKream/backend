package com.v1.iskream.layer.domain.dto.response;

import lombok.Getter;

@Getter
public class ThumbnailResponseDto {
    private String imgUrl;

    public ThumbnailResponseDto(String url) {
        this.imgUrl = url;
    }
}
