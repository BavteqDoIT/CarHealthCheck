package bavteqdoit.carhealthcheck.data;

import bavteqdoit.carhealthcheck.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
