package bavteqdoit.carhealthcheck.web;

import bavteqdoit.carhealthcheck.data.UserRepository;
import bavteqdoit.carhealthcheck.model.Brand;
import bavteqdoit.carhealthcheck.model.User;
import bavteqdoit.carhealthcheck.service.BrandService;
import bavteqdoit.carhealthcheck.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final BrandService brandService;

    public AdminController(UserService userService, UserRepository userRepository, BrandService brandService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.brandService = brandService;
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }

    @GetMapping("/admin/users")
    public String usersPage(Model model) {
        model.addAttribute("users", userService.findAll());
        return "adminUsers";
    }

    @PostMapping("/admin/users/{id}/role")
    public String changeUserRole(@PathVariable Long id, @RequestParam String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika o id: " + id));

        if(user.getUsername().equals(currentUsername)) {
            return "redirect:/admin/users?error=cannotChangeOwnRole";
        }

        user.setRole(role);
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("admin/brands")
    public String brandsPage(Model model) {
        model.addAttribute("brands", brandService.findAll());
        return "adminBrands";
    }

    @GetMapping("/admin/brands/edit/{id}")
    public String editBrandForm(@PathVariable Long id, Model model) {
        Brand brand = brandService.findById(id);
        model.addAttribute("brand", brand);
        return "adminBrandEdit";
    }

    @PostMapping("/admin/brands/edit/{id}")
    public String updateBrand(@PathVariable Long id, @RequestParam String name) {
        Brand brand = brandService.findById(id);
        brand.setBrandName(name);
        brandService.save(brand);
        return "redirect:/admin/brands";
    }

    @PostMapping("/admin/brands/delete/{id}")
    public String deleteBrand(@PathVariable Long id) {
        brandService.deleteById(id);
        return "redirect:/admin/brands";
    }

    @GetMapping("admin/models")
    public String modelsPage() { return "adminModels"; }

    @GetMapping("admin/engines")
    public String enginesPage() { return "adminEngines"; }
}