package bavteqdoit.carhealthcheck.web;

import bavteqdoit.carhealthcheck.model.User;
import bavteqdoit.carhealthcheck.service.AuthService;
import bavteqdoit.carhealthcheck.dto.RegisterResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") User user, Model model) {
        RegisterResult result = authService.register(user);

        if (!result.isSuccess()) {
            model.addAttribute("error", result.getError().getMessageKey());
            return "register";
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}
