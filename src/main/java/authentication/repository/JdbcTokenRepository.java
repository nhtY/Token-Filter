package authentication.repository;

import authentication.records.TokenRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class JdbcTokenRepository implements ITokenRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcTokenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public TokenRecord saveToken(TokenRecord tokenRecord) {

        if(!isTokenExists(tokenRecord.uid())){

            Object[] params = {tokenRecord.uid(), tokenRecord.token()};
            int[] types = {Types.INTEGER, Types.VARCHAR};

            jdbcTemplate.update("insert into tokens (uid, token) values(?, ?)",
                    params, types);
        }else {
            Object[] params = {tokenRecord.token(), tokenRecord.uid()};
            int[] types = { Types.VARCHAR, Types.INTEGER};

            jdbcTemplate.update("update tokens set token=? where uid=? ",
                    params, types);
        }


        return tokenRecord;
    }

    private boolean isTokenExists(Integer id) {
        List<TokenRecord> results = jdbcTemplate.query("select * from tokens where uid = ?",
                this::mapToTokenRecord, id);

        return results.size() != 0;
    }

    @Override
    public int deleteToken(TokenRecord tokenRecord) {

        Object[] params = {tokenRecord.uid(), tokenRecord.token()};
        int[] types = {Types.INTEGER, Types.VARCHAR};

        int affectedRowsCount = jdbcTemplate.update("delete from tokens where uid = ? and token = ?",
                params, types);

        log.info("{} rows deleted from tokens.", affectedRowsCount);

        return affectedRowsCount;
    }

    @Override
    public Optional<TokenRecord> findToken(String token) {

        List<TokenRecord> results = jdbcTemplate.query("select * from tokens where token = ?",
                this::mapToTokenRecord, token);

        return results.size() == 0? Optional.empty() : Optional.of(results.get(0));
    }


    private TokenRecord mapToTokenRecord(ResultSet row, int rowNum) throws SQLException {
        return new TokenRecord(
                row.getInt("uid"),
                row.getString("token")
        );
    }
}
