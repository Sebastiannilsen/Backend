package no.ntnu.bicycle.service;

import no.ntnu.bicycle.model.Product;
import no.ntnu.bicycle.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Business logic related to products
 */
@Service
public class ProductService {
    @Autowired
    private final ProductRepository productRepository;

    /**
     * Constructor for product service
     * @param productRepository ProductService
     */
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Makes iterable to list of products
     * @param iterable Iterable<Product>
     * @return list of products
     */
    public List<Product> iterableToList(Iterable<Product> iterable) {
        List<Product> list = new LinkedList<>();
        iterable.forEach(list::add);
        return list;
    }

    /**
     * Gets all products
     * @return list of products
     */
    public List<Product> getAllProducts() {return iterableToList(productRepository.findAll());}


    /**
     * Adding a product
     * @param product Product
     * @return true if product added, false if not
     */
    public boolean addNewProduct(Product product) {
        boolean added = false;
        if (product != null) {
            productRepository.save(product);
            added = true;
        }
        return added;
    }


    /**
     * Deleting product
     * @param product Product
     * @return true if deleted, false if not
     */
    public boolean deletingProduct(Product product) {
        boolean deleted = false;
        if (product != null) {
            productRepository.delete(product);
            deleted = true;
        }
        return deleted;
    }

    public String updateProduct(int id, Product product) {
        Product existingProduct = this.getProductById(id);
        String errorMessage = null;
        if (existingProduct == null) {
            errorMessage = "No customerOrder with " + id + "found";
        }
        if (product == null) {
            errorMessage = "Wrong data in request body";
        } else if (product.getId() != id) {
            errorMessage = "Wrong id, does not match";
        }

        if (errorMessage == null) {
            productRepository.save(product);
        }
        return errorMessage;
    }

    /**
     * Finds order by ID
     * @param id Integer
     * @return The author or null if none found by that ID
     */
    public Product getProductById(int id) {
        if (productRepository.findById(id).isPresent()) {
            return productRepository.findById(id).get();
        }else{
            return null;
        }
    }

    public List<Product> getAllProductsByCustomer(String customer) {
        return null;
    }

    public List<Product> getAllProductsByOrder(String order) {
        return null;
    }

    public List<Product> getAllProductsByCustomerAndOrder(String customer, String order) {
        return null;
    }


}

