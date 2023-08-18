package authentication.service;

import authentication.error.DeleteError;
import authentication.error.UpdateError;
import authentication.model.Product;
import authentication.repository.IProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService {

    private final IProductRepository  productRepo;

    public ProductService(IProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public List<Product> getAll() {
        return productRepo.fetchAll();
    }

    @Override
    public Product findById(Integer pid) {
        return productRepo.findById(pid).orElse(null);
    }

    @Override
    public void delete(Integer pid) {
        int affectedRowCount = productRepo.delete(pid);

        if (affectedRowCount == 0){
            throw new DeleteError();
        }
    }

    @Override
    public Product update(Integer pid, Product patch) {
        Product product = productRepo.findById(pid).orElse(null);

        if (product != null) {
            product.setTitle(patch.getTitle());
            product.setDescription(patch.getDescription());
            product.setImgUrl(patch.getImgUrl());
            product.setPrice(patch.getPrice());

            return productRepo.update(product);
        }

        throw new UpdateError();
    }
}
