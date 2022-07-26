package com.innovation.myblog.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UpdateMyblogDto {
    private final String title;
    private final String content;
    private String ImageUrl;
    private final int commentCount;
}
