package no.ntnu.bicycle.repository;

import no.ntnu.bicycle.model.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    /**
     * Gets a customer that has this email
     * @param email email to look for
     * @return the user that has this email
     */
    @Query(value = "SELECT * FROM public.customer where email = :email", nativeQuery = true)
    Optional<Customer> findByEmail(@Param("email") String email);

    Optional<Customer> findByEmailEqualsIgnoreCase(@NonNull String email);

    @Query(value = "SELECT * FROM public.customer where last_name = :last_name", nativeQuery = true)
    Optional<Customer> findByLastName(@Param("last_name") String lastName);

    @Query(value = "SELECT * FROM public.customer where first_name = :first_name", nativeQuery = true)
    Optional<Customer> findByFirstName(@Param("first_name") String firstName);

}

