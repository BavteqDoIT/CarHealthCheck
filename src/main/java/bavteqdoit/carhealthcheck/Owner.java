package bavteqdoit.carhealthcheck;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Owner {
    @NotBlank(message="Providing your name and surname is mandatory.")
    private String name;
    @NotBlank(message="Providing your address is mandatory.")
    private String street;
    @NotBlank(message="Providing your city is mandatory.")
    private String city;
    @NotBlank(message="Providing your state is mandatory.")
    private String state;
    @NotBlank(message="Providing your zip code is mandatory.")
    private String zip;
    @NotBlank(message = "Providing your phone number is mandatory.")
    @Size(min = 9, max = 9, message = "Phone number must be exactly 9 digits long")
    private String phoneNumber;
}
