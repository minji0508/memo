package com.meta.memo.controller;

import com.meta.memo.domain.Memo;
import com.meta.memo.dto.MemoRequestDto;
import com.meta.memo.dto.MemoResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/memos")
public class MemoController {
    //임시 데이터베이스(내장 메모리인 해시맵 활용)
    private final Map<Long, Memo> memoList = new HashMap<>();

    @PostMapping
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto memoRequestDto){
        //RequestDto -> Entity 변환
        Memo memo = new Memo(memoRequestDto);

        //(임시) 현재 저장된 Memo들의 최대 id 체크
        Long maxId = memoList.size() >0 ? Collections.max(memoList.keySet()) + 1 : 1;
        memo.setId(maxId);

        // DB 저장
        memoList.put(memo.getId(), memo);

        //Entity -> ResponseDto 변환
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);
        return memoResponseDto;

    }

    @GetMapping()
    public List<MemoResponseDto> getMemos(){
        //Map to List
        List<MemoResponseDto> responseDtoList = memoList.values().stream().map(MemoResponseDto::new).toList();
        return responseDtoList;
    }

}
