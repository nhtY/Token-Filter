package authentication.controller;

import authentication.model.Product;
import authentication.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final IProductService productService;

    @Autowired
    public ProductController(IProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public List<Product> getAll(){
        return productService.getAll();
    }

    @GetMapping(path = "/{pid}")
    public Product findById(@PathVariable Integer pid) {
        return productService.findById(pid);
    }

    @DeleteMapping(path = "/{pid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer pid, @RequestHeader("my-token") String token) {

//        if (tokenUtil.isTokenExpired(token) || !tokenUtil.isThisAdmin(token)) {
//            throw new UnauthorizedUser();
//        }

        productService.delete(pid);

    }

    @PatchMapping(path = "/{pid}")
    public Product update(@PathVariable Integer pid, @RequestBody Product product, @RequestHeader("my-token") String token) {

//        if (tokenUtil.isTokenExpired(token) || !tokenUtil.isThisAdmin(token)) {
//            throw new UnauthorizedUser();
//        }
        return productService.update(pid, product);
    }
}
