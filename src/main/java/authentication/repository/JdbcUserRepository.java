package authentication.repository;

import authentication.error.TokenNotFoundError;
import authentication.error.UpdateError;
import authentication.model.User;
import authentication.records.PagedResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class JdbcUserRepository implements IUserRepository {
    private static final int PAGE_SIZE = 6;
    private static int TOTAL_PAGE_NUMBER = 0;
    private JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate template) {
        this.jdbcTemplate =  template;
    }

    @Override
    public Optional<User> findUserById(Integer id) {
        List<User> results = jdbcTemplate.query("SELECT * FROM user WHERE id=?",
                this::mapRowToUser,
                id
        );
        log.info("User with id: {} is/are: {}", id, results);
        return results.size() == 0? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public Optional<User> findUser(String username, String password) {
        List<User> results = jdbcTemplate.query("SELECT * FROM user WHERE username = ? AND password = sha1(?)",
                this::mapRowToUser,
                username,
                password
        );
        log.info("User with username: {}, password: {} is/are: {}", username, password, results);
        return results.size() == 0? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        List<User> results = jdbcTemplate.query("SELECT * FROM user WHERE username = ?",
                this::mapRowToUser,
                username
        );
        log.info("User(s) with this '{}' username is: {}", username, results);
        return results.size() == 0? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<User> fetchAll() {
        List<User> users = jdbcTemplate.query("select * from user", this::mapRowToUser);
        log.info("Fetch all users: {}", users);
        return users;
    }

    @Override
    public PagedResult<User> fetchAllPaged(int page) {
        Integer totalItemCount = jdbcTemplate.query("select count(*) from user",
                rs -> {
            rs.next();
            return rs.getInt(1);
        });

        if(totalItemCount != 0) {
            TOTAL_PAGE_NUMBER = (int) Math.ceil((double)totalItemCount / PAGE_SIZE);
            log.info("Total users: {}, TOTAL_PAGE_NUMBER: {}, a math: {}", totalItemCount, TOTAL_PAGE_NUMBER, Math.ceil(7/6));
        }

        if (page <= TOTAL_PAGE_NUMBER) {
            List<User> users = jdbcTemplate.query("select * from user order by id limit ?,?",
                    this::mapRowToUser,
                    page * PAGE_SIZE,
                    PAGE_SIZE);
            log.info("Fetch all users for page {}: {}", page, users);
            return new PagedResult<>(totalItemCount, PAGE_SIZE, page, TOTAL_PAGE_NUMBER, users);
        }

        throw new RuntimeException("Provided bad page number data");
    }

    @Override
    public Optional<User> getUserIfTokenExists(String token) {
        List<User> results = jdbcTemplate.query("select *  from user " +
                "right join tokens on user.id = tokens.uid " +
                "where tokens.token= ?",
                this::mapRowToUser,
                token);
        log.info("Users found for the given token {} are: {}", token, results);
        return results.size() == 0? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public User save(User user) {
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "insert into user (username, password, role, name, surname)" +
                        " values(?, sha1(?), ?, ?, ?)",
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR
        );

        // Generated id value is needed to use it to return the User instance:
        pscf.setReturnGeneratedKeys(true);

        // Give the parameters to the prepared statement:
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        user.getUsername(),
                        user.getPassword(),
                        user.getRole(),
                        user.getName(),
                        user.getSurname()
                )
        );

        // Create a GeneratedKeyHolder to keep the generated value:
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        // execute the query:
        jdbcTemplate.update(psc, keyHolder);

        // get the returned value:
        Integer id = keyHolder.getKey().intValue();

        // set the id of the user:
        user.setId(id);

        log.info("User saved: {}", user);
        // now return the saved user
        return user;
    }

    @Override
    public User updateUser(User user) {
        Object[] params = {user.getUsername(), user.getPassword(), user.getName(),
                user.getSurname(), user.getRole(), user.getId()};
        int[] types = {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER};
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "update user set username=?, password=sha1(?), name=?, surname=?, role=? where id=?",
                types
        );

        // Give the parameters to the prepared statement:
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(params)
        );

        int affectedRowCount = jdbcTemplate.update(psc);

        if (affectedRowCount == 0) throw new UpdateError();

        return  user;
    }

    @Override
    public Integer deleteById(Integer id) {
        Object[] params = { id };
        int[] types = {Types.INTEGER };
        int affectedRowsCount = jdbcTemplate.update("DELETE FROM user WHERE id=?", params, types);

        log.info("{} row(s) deleted from user", affectedRowsCount);

        return affectedRowsCount;
    }

    @Override
    public Boolean isAdmin(String token) {

        List<String> roles = jdbcTemplate.query("select role  from user " +
                "right join tokens on user.id = tokens.uid" +
                " where tokens.token=?", (rs, rowNum) -> rs.getString("role"), token);

        log.info("This token {} belongs to a {}", token, roles);
        if (roles.size() == 0) {
            throw new TokenNotFoundError();
        }

        return roles.get(0).equals("ADMIN");
    }

    private User mapRowToUser(ResultSet row, int rowNum) throws SQLException {
        return new User(
                row.getInt("id"),
                row.getString("username"),
                row.getString("password"),
                User.Role.valueOf(row.getString("role")),
                row.getString("name"),
                row.getString("surname")
        );
    }
}