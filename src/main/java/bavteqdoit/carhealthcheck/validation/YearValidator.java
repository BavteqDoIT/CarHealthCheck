package bavteqdoit.carhealthcheck.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class YearValidator implements ConstraintValidator<Year, Integer> {

    private int min;
    private boolean allowFuture;

    @Override
    public void initialize(Year constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.allowFuture = constraintAnnotation.allowFuture();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) return true;

        int currentYear = LocalDate.now().getYear();
        boolean notTooOld = value >= min;
        boolean notInFuture = allowFuture || value <= currentYear;

        return notTooOld && notInFuture;
    }
}
