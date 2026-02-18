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
}
