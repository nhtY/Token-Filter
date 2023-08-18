package authentication.service;

import authentication.model.Product;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface IProductService {

    List<Product> getAll();
    Product findById(Integer pid);
    void delete(Integer pid);
    Product update(Integer pid, Product patch);
}
