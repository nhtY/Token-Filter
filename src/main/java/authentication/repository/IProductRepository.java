package authentication.repository;

import authentication.model.Product;

import java.util.List;
import java.util.Optional;

public interface IProductRepository {

    List<Product> fetchAll();

    Optional<Product> findById(Integer id);

    int delete(Integer id);

    Product update(Product product);

}
