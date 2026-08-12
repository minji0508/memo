package com.meta.memo.controller;

import com.meta.memo.domain.Memo;
import com.meta.memo.dto.MemoRequestDto;
import com.meta.memo.dto.MemoResponseDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@RestController
@RequestMapping("api/memos")
public class MemoController {
    private JdbcTemplate jdbcTemplate;

    public  MemoController(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    @PostMapping
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto memoRequestDto){
        //RequestDto -> Entity 변환
        Memo memo = new Memo(memoRequestDto);


        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본키(id)를 반환 받기 위한 객체

        String sql = "INSERT INTO memo (username, contents) VALUES (?, ?)";
        jdbcTemplate.update(con-> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, memo.getUsername());
            preparedStatement.setString(2, memo.getContents());
            return preparedStatement;
        }, keyHolder);

        // [임시] DB INSERT 후 받아온 기본 키 확인
        Long id = keyHolder.getKey().longValue();
        memo.setId(id);

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

    @PutMapping("{id}")
    public Long updateMemo(
            @PathVariable Long id,
            @RequestBody MemoRequestDto memoRequestDto){
        //해당 id의 메모가 데이터베이스에 존재하는지 확인
        if (memoList.containsKey(id)){
            //해당 메모를 가져오기
            Memo memo = memoList.get(id);

            //메모 수정
            memo.update(memoRequestDto);
            return  memo.getId();
        } else{
            throw new IllegalArgumentException("선택한 id의 메모는 존재하지 않습니다.");
        }
    }

    @DeleteMapping("{id}")
    public Long deleteMemo(@PathVariable Long id){
        //해당 id의 메모가 데이터베이스에 존재하는지 확인
        if (memoList.containsKey(id)){
            // 메모 삭제
            memoList.remove(id);
            return id;
        } else{
            throw new IllegalArgumentException("선택한 id의 메모는 존재하지 않습니다.");
        }
    }
}
