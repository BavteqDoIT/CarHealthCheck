package bavteqdoit.carhealthcheck.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = YearValidator.class)
public @interface Year {
    String message() default "{car.year.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int min() default 1900;

    boolean allowFuture() default false;
}
