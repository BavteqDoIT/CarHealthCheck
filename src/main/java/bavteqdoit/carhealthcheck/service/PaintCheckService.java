package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.CarRepository;
import bavteqdoit.carhealthcheck.model.Car;
import bavteqdoit.carhealthcheck.model.PaintCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaintCheckService {

    private final CarService carService;
    private final CarRepository carRepository;

    @Transactional(readOnly = true)
    public PaintFormData preparePaintForm(Long carId, String username) {
        Car car = carService.getUserCar(carId, username);

        PaintCheck paintCheck = car.getPaintCheck();
        if (paintCheck == null) {
            paintCheck = new PaintCheck();
            paintCheck.setCar(car);
        }

        return new PaintFormData(car, paintCheck);
    }

    public record PaintFormData(Car car, PaintCheck paintCheck) {}

    @Transactional
    public void savePaintCheck(Long carId, String username, PaintCheck paintCheck) {

        Car car = carService.getUserCar(carId, username);

        paintCheck.setCar(car);

        if (paintCheck.getDamages() != null) {
            paintCheck.getDamages().forEach(d -> d.setPaintCheck(paintCheck));
        }

        car.setPaintCheck(paintCheck);

        carRepository.save(car);
    }
}
