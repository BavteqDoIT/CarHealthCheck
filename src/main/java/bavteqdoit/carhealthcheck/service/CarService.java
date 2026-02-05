package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.CarRepository;
import bavteqdoit.carhealthcheck.model.Car;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;

    @Transactional
    public void deleteUserCar(Long carId, String username) {
        Car car = carRepository.findByIdAndOwner_Username(carId, username)
                .orElseThrow(() -> new IllegalArgumentException("account.car.delete.notFound"));

        carRepository.delete(car);
    }
}
