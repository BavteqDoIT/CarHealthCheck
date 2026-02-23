package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.CarRepository;
import bavteqdoit.carhealthcheck.data.UserRepository;
import bavteqdoit.carhealthcheck.model.Car;
import bavteqdoit.carhealthcheck.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final UserRepository userRepository;

    @Transactional
    public Car createForUsername(Car car, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        car.setOwner(user);
        return carRepository.save(car);
    }

    @Transactional
    public void deleteUserCar(Long carId, String username) {
        Car car = getUserCar(carId, username);
        carRepository.delete(car);
    }

    @Transactional(readOnly = true)
    public Car getUserCar(Long carId, String username) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!car.getOwner().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return car;
    }

    @Transactional(readOnly = true)
    public List<Car> getUserCars(String username) {
        return carRepository.findAllByOwner_Username(username);
    }
}
