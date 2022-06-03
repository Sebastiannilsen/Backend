package no.ntnu.bicycle.repository;

import no.ntnu.bicycle.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<CustomerOrder, Integer> {
/*
    @Query(value = "SELECT * FROM public.customer_order where date_and_time = :dateAndTime", nativeQuery = true)
    Optional<CustomerOrder> findByDateAndTime(LocalDateTime dateAndTime);
*/

    @Query(value = "SELECT * FROM public.customer_order where email = :email", nativeQuery = true)
    Optional<CustomerOrder> findByEmail(String email);

}
