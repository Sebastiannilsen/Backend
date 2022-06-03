package no.ntnu.bicycle.repository;

import no.ntnu.bicycle.model.Bicycle;
import org.springframework.data.repository.CrudRepository;

public interface BicycleRepository extends CrudRepository<Bicycle, Long> {
}
