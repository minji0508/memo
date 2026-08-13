package com.meta.memo.repository;

import com.meta.memo.domain.Memo;
import com.meta.memo.dto.MemoRequestDto;
import com.meta.memo.dto.MemoResponseDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MemoRepository {
    private final JdbcTemplate jdbcTemplate;

    public MemoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Memo save(Memo memo) {
        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본키(id)를 반환 받기 위한 객체

        String sql = "INSERT INTO memo (username, contents) VALUES (?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, memo.getUsername());
            preparedStatement.setString(2, memo.getContents());
            return preparedStatement;
        }, keyHolder);

        // [임시] DB INSERT 후 받아온 기본 키 확인
        Long id = keyHolder.getKey().longValue();
        memo.setId(id);

        return memo;
    }

    public List<MemoResponseDto> findAll() {
        //DB 조회
        String sql = "SELECT * FROM memo";

        List<MemoResponseDto> responseDtoList = jdbcTemplate.query(sql, new RowMapper<MemoResponseDto>() {
            @Override
            public MemoResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long id = rs.getLong("id");
                String username = rs.getString("username");
                String contents = rs.getString("contents");
                return new MemoResponseDto(id, username, contents);
            }
        });

        return responseDtoList;
    }

    public Long updateMemo(Long id, MemoRequestDto memoRequestDto) {
        String sql = "UPDATE memo SET username = ?, contents =? WHERE id =?";
        jdbcTemplate.update(sql, memoRequestDto.getUsername(), memoRequestDto.getContents(), id);
        return id;
    }

    public Long delete(Long id) {
        String sql = "DELETE FROM memo WHERE id = ?";
        jdbcTemplate.update(sql, id);
        return id;
    }



public Memo findById(Long id) {
    //DB조회
    String sql = "SELECT * FROM memo WHERE id =?";

    return jdbcTemplate.query(sql, resultSet -> {
        if (resultSet.next()) {
            Memo memo = new Memo();
            memo.setUsername(resultSet.getString("username"));
            memo.setContents(resultSet.getString("contents"));
            return memo;
        } else {
            return null;
        }
    }, id);
}

}




