package bavteqdoit.carhealthcheck;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Owner {
    @NotBlank(message="Podanie imienia i nazwiska jest obowiązkowe.")
    private String name;
    @NotBlank(message="Podanie ulicy jest obowiązkowe.")
    private String street;
    @NotBlank(message="Podanie miejscowości jest obowiązkowe.")
    private String city;
    @NotBlank(message="Podanie województwa jest obowiązkowe.")
    private String state;
    @NotBlank(message="Podanie kodu pocztowego jest obowiązkowe.")
    private String zip;
    @Digits(integer=9, fraction=0, message="Nieprawidłowy nr telefonu.")
    private String tel;
}
