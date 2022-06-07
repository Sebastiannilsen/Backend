package no.ntnu.bicycle.controller;

import no.ntnu.bicycle.model.Product;
import no.ntnu.bicycle.service.CustomerService;
import no.ntnu.bicycle.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API controller for product.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final CustomerService customerService;

    /**
     * Constructor with parameters
     * @param productService product service
     * @param customerService customer service
     */
    public ProductController(ProductService productService, CustomerService customerService) {
        this.productService = productService;
        this.customerService = customerService;
    }

    /**
     * Gets all products
     * @return list of all products.
     */
    @GetMapping()
    public List<Product> getAllProducts(@RequestParam(required = false) String customer,
                                        @RequestParam(required = false) String order) {
        if (order != null && !"".equals(order)) {
            if (customer != null && !"".equals(customer)) {
                return productService.getAllProductsByCustomerAndOrder(customer, order);
            } else {
                return productService.getAllProductsByCustomer(customer);
            }
        } else if(customer != null && !"".equals(customer)) {
            return productService.getAllProductsByOrder(order);
        } else { return productService.getAllProducts();
        }
    }

    /**
     * Gets one product
     * @param id Integer. ID of the product.
     * @return 200 OK status on success, or 404 not found if it does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getOne(@PathVariable Integer id) {
        ResponseEntity<Product> response;
        Product product = productService.getProductById(id);
        if (product != null) {
            response = new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }


    /**
     * Adds a new product
     * @param product Product
     * @return HTTP 201 CREATED id added, else 400 bad request.
     */
    @PostMapping()
    public ResponseEntity<String> addNewProduct(@RequestBody Product product) {
        ResponseEntity<String> response;
        String errorMessage = productService.addNewProduct(product);
        if (errorMessage == null) {
            response = new ResponseEntity<>("Product " + product.getProductName() +
                    " successfully added.", HttpStatus.CREATED);
        } else {
            response = new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        return response;
    }



    /**
     * Deletes a product with selected id
     * @return HTTP 200 OK if product deleted, HTTP not found if it did not get deleted.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id) {
        ResponseEntity<String> response;
        Product product = productService.getProductById(id);
        String errorMessage = productService.deletingProduct(product);
        if (errorMessage == null) {
            response = new ResponseEntity<>("Product with id " + product.getId() +
                    " successfully deleted.", HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }
        return response;
    }


}


