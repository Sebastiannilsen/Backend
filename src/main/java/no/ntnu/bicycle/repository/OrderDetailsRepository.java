package no.ntnu.bicycle.repository;

import no.ntnu.bicycle.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {
    /*@Query(value = "SELECT * FROM public.order_details where order_number = :order_number")
    Optional<OrderDetails> findByOrderNumber(String orderNumber);

    @Query(value = "SELECT * FROM public.order_details where product_code = :productCode")
    Optional<OrderDetails> findByProductCode(@Param("product_code") String productCode);

    @Query(value = "SELECT * FROM public.order_details where price_each = :priceEach")
    Optional<OrderDetails> findByPriceEach(@Param("price_each") String priceEach);*/
}
