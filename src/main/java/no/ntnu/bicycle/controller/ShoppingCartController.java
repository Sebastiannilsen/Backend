package no.ntnu.bicycle.controller;

import no.ntnu.bicycle.model.Customer;
import no.ntnu.bicycle.model.Product;
import no.ntnu.bicycle.service.CustomerService;
import no.ntnu.bicycle.service.ProductService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {

    private final CustomerService customerService;
    private final ProductService productService;

    public ShoppingCartController(CustomerService customerService, ProductService productService){
        this.customerService = customerService;
        this.productService = productService;
    }




    /**
     * Add product to cart
     * @param http the id of the product
     * @return 200 OK if product added to cart, 400 bad request if not
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    public ResponseEntity<String> addProductToCart(HttpEntity<String> http){
        try {
            JSONObject json = new JSONObject(http.getBody());

            int id = json.getInt("productId");

            Product product = productService.getProductById(id);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            if (email.equals("anonymousUser")){
                return new ResponseEntity<>("Need to be logged in", HttpStatus.UNAUTHORIZED);
            }else{
                Customer customer = customerService.findCustomerByEmail(email);
                customer.addProductToShoppingCart(product);
                customerService.updateCustomer(customer);
                return new ResponseEntity<>("Product added to cart",HttpStatus.OK);
            }
        }catch (JSONException e){
            return new ResponseEntity<>("Posted fields are invalid",HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes a product in the cart
     * @param http the id of the product
     * @return
     */
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping()
    public ResponseEntity<String> deleteProductInCart(HttpEntity<String> http) {
        ResponseEntity<String> response;

        JSONObject json = new JSONObject(http.getBody());

        int id = json.getInt("productId");
        try {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();

            Customer customer = customerService.findCustomerByEmail(email);

            if (customer != null && customer.isValid()) {
                customer.removeFromShoppingCart(productService.getProductById(id));
                customerService.updateCustomer(customer);
                response = new ResponseEntity<>("Product removed from cart",HttpStatus.OK);
            } else {
                response = new ResponseEntity<>("Not logged in",HttpStatus.BAD_REQUEST);
            }
        }catch (JSONException e){
            response = new ResponseEntity<>("Posted fields are invalid",HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Get products in cart
     * @return 200 OK status on success, 400 bad request if it does not get products in cart
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping()
    public ResponseEntity<List<Product>> getProductsInCart(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Customer customer = customerService.findCustomerByEmail(email);
        List<Product> products = customer.getShoppingCart();
        if (!products.isEmpty()) {
            return new ResponseEntity<>(products,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
