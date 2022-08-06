package com.innovation.myblog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {

    private  Long postid;

    private Long parentid;

    private  String comment;

    private String author;


}
