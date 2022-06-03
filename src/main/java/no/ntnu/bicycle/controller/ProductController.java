package no.ntnu.bicycle.controller;

import no.ntnu.bicycle.model.Customer;
import no.ntnu.bicycle.model.Product;
import no.ntnu.bicycle.service.CustomerService;
import no.ntnu.bicycle.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Properties;

/**
 * REST API controller for product.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private ProductService productService;
    private CustomerService customerService;

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
     * @return list of all products
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
        Product product = productService.findOrderById(id);
        if (product != null) {
            response = new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }


    @PostMapping()
    public ResponseEntity<String> addNewProduct(@RequestBody Product product) {
        ResponseEntity<String> response;

        Product existingProduct = productService.getProductById(product.getId());
        if (product.isValid()) {
            if (existingProduct == null) {
                if (productService.addNewProduct(product)) {
                    response = new ResponseEntity<>(HttpStatus.OK);
                } else {
                    response = new ResponseEntity<>("Product could not be added", HttpStatus.BAD_REQUEST);
                }
            } else {
                response = new ResponseEntity<>("Product already exist", HttpStatus.BAD_REQUEST);
            }
        }else{
            response = new ResponseEntity<>("Empty body in request", HttpStatus.BAD_REQUEST);
        }
        return response;
    }


    /**
     * Add product to cart
     * @param idJsonObject String
     * @return 200 OK if product added to cart, 400 bad request if not
     */
    @PostMapping(value = "/addToCart", consumes = "application/json")
    public ResponseEntity<String> addProductToCart(@RequestBody String idJsonObject){
        try {
            String[] stringArray = idJsonObject.split("\"" );
            int id = Integer.parseInt(stringArray[3]);

            Product product = productService.findOrderById(id);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            if (email.equals("anonymousUser")){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }else{

                Customer customer = customerService.findCustomerByEmail(email);

                customer.addProductToShoppingCart(product);

                customerService.updateCustomer(customer.getId(), customer);

                return new ResponseEntity<>(HttpStatus.OK);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get products in cart
     * @return 200 OK status on success, 400 bad request if it does not get products in cart
     */
    @GetMapping(value = "/shopping-cart", produces = "application/json")
    public ResponseEntity<List<Product>> getProductsInCart(){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            Customer customer = customerService.findCustomerByEmail(email);
            List<Product> products = customer.getShoppingCart();
            return new ResponseEntity<>(products,HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Deletes product
     * @param product Product
     * @return HTTP 200 OK if product deleted, HTTP not found if it did not get deleted
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Product product) {
        ResponseEntity<String> response;
        if (productService.deletingProduct(product)) {
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }



}


