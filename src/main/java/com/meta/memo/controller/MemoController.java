package com.meta.memo.controller;

import com.meta.memo.dto.MemoRequestDto;
import com.meta.memo.dto.MemoResponseDto;
import com.meta.memo.service.MemoService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/memos")
public class MemoController {
    private  final MemoService memoService;

    public MemoController(JdbcTemplate jdbcTemplate) {
        this.memoService = new MemoService(jdbcTemplate);
    }

    @PostMapping
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto memoRequestDto) {
        return memoService.createMemo(memoRequestDto);
    }

    @GetMapping()
    public List<MemoResponseDto> getMemos() {

        return memoService.getMemos();
    }

    @PutMapping("{id}")
    public Long updateMemo(
            @PathVariable Long id,
            @RequestBody MemoRequestDto memoRequestDto) {
        return memoService.updateMemo(id, memoRequestDto);

    }

    @DeleteMapping("{id}")
    public Long deleteMemo(@PathVariable Long id) {
        return memoService.deleteMemo(id);
    }
}
