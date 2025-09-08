package bavteqdoit.carhealthcheck.web;

import bavteqdoit.carhealthcheck.model.User;
import bavteqdoit.carhealthcheck.data.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {

    private final UserRepository userRepository;

    public AccountController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/account")
    public String getUserCars(@AuthenticationPrincipal org.springframework.security.core.userdetails.User authUser,
                              Model model) {
        User user = userRepository.findByUsername(authUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("cars", user.getCars());
        return "account";
    }
}
