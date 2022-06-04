package no.ntnu.bicycle.repository;

import no.ntnu.bicycle.model.BicycleRentalOrder;
import org.springframework.data.repository.CrudRepository;

public interface BicycleRentalOrderRepository extends CrudRepository<BicycleRentalOrder, Long> {
}
