package com.meta.memo.dto;

import com.meta.memo.domain.Memo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemoResponseDto {
    private Long id;
    private String username;
    private String contents;

    public MemoResponseDto(Memo memo){
        this.id = memo.getId();
        this.username=memo.getUsername();
        this.contents = memo.getContents();
    }
}
