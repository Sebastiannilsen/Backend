package no.ntnu.bicycle.repository;

import no.ntnu.bicycle.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
    /*@Query(value = "SELECT * FROM public.order where product_name = :product_name", nativeQuery = true)
    List<Product> findByProductName(String product);*/
}
