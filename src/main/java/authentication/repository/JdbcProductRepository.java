package authentication.repository;

import authentication.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class JdbcProductRepository implements IProductRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public List<Product> fetchAll() {
        return jdbcTemplate.query("select * from products", this::mapRowToProduct);
    }

    @Override
    public Optional<Product> findById(Integer id) {
        List<Product> results = jdbcTemplate.query("select * from products where pid = ?", this::mapRowToProduct, id);
        return results.size() == 0? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public int delete(Integer id) {
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "delete from products where pid = ?", Types.INTEGER
        );

        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        id
                )
        );

        int affectedRowCount = jdbcTemplate.update(psc);

        log.info("{} product deleted: id={}", affectedRowCount, id);

        return affectedRowCount;
    }

    @Override
    public Product update(Product product) {
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "update products set title=?, imgUrl=?, description=?, price=? where pid=?",
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.DOUBLE, Types.INTEGER
        );

        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        product.getTitle(),
                        product.getImgUrl(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getPid()
                )
        );

        int affectedRowCount = jdbcTemplate.update(psc);

        if (affectedRowCount== 0){
            throw new RuntimeException("Update failed");
        }

        return product;
    }

    private Product mapRowToProduct(ResultSet row, int rowNum) throws SQLException {
        return new Product(
                row.getInt("pid"),
                row.getString("title"),
                row.getString("imgUrl"),
                row.getString("description"),
                row.getDouble("price")
        );
    }
}
