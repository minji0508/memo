package com.meta.memo.service;

import com.meta.memo.domain.Memo;
import com.meta.memo.dto.MemoRequestDto;
import com.meta.memo.dto.MemoResponseDto;
import com.meta.memo.repository.MemoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemoService {
    private final MemoRepository memoRepository;

    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    @Transactional
    public MemoResponseDto createMemo(MemoRequestDto memoRequestDto) {
        Memo memo = new Memo(memoRequestDto);
        Memo savedmemo = memoRepository.save(memo);
        MemoResponseDto memoResponseDto = new MemoResponseDto(savedmemo);
        return memoResponseDto;
    }

    @Transactional(readOnly = true)
    public List<MemoResponseDto> getMemos() {
        List<MemoResponseDto> responseDtoList = memoRepository.findAll()
                .stream().map(MemoResponseDto::new).toList();
        return responseDtoList;
    }

    @Transactional
    public Long updateMemo(Long id, MemoRequestDto memoRequestDto) {
        Memo foundMemo = findMemoById(id);
        foundMemo.update(memoRequestDto);
        return id;
    }

    @Transactional
    public Long deleteMemo(Long id) {
        Memo foundMemo = findMemoById(id);
        memoRepository.delete(foundMemo);
        return id;
    }

    private Memo findMemoById(Long id) {
        return memoRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 id의 메모는 존재하지 않습니다. "));
    }


}


