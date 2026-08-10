package com.meta.memo.dto;

import lombok.Getter;

@Getter
public class MemoRequestDto {
    private Long id;
    private String username;
    private String contents;
}
