package com.innovation.myblog.models;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MyblogDto {

    private final String title;

    private final String content;

    private final String author;

    private final String password;


}
