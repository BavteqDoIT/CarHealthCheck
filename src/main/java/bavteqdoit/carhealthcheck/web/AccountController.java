package bavteqdoit.carhealthcheck.web;

import bavteqdoit.carhealthcheck.model.User;
import bavteqdoit.carhealthcheck.data.UserRepository;
import bavteqdoit.carhealthcheck.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final UserRepository userRepository;
    private final CarService carService;

    @GetMapping("/account")
    public String getUserCars(@AuthenticationPrincipal org.springframework.security.core.userdetails.User authUser,
                              Model model) {
        User user = userRepository.findByUsername(authUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("cars", user.getCars());
        return "account";
    }

    @PostMapping("/account/cars/{id}/delete")
    public String deleteCar(@AuthenticationPrincipal org.springframework.security.core.userdetails.User authUser,
                            @PathVariable Long id,
                            RedirectAttributes ra) {

        try {
            carService.deleteUserCar(id, authUser.getUsername());
            ra.addFlashAttribute("successMessage", "account.car.delete.success");
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/account";
    }
}

