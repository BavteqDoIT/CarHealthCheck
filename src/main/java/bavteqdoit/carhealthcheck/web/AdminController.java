package bavteqdoit.carhealthcheck.web;

import bavteqdoit.carhealthcheck.data.UserRepository;
import bavteqdoit.carhealthcheck.model.Brand;
import bavteqdoit.carhealthcheck.model.EngineType;
import bavteqdoit.carhealthcheck.model.FuelType;
import bavteqdoit.carhealthcheck.model.User;
import bavteqdoit.carhealthcheck.service.BrandService;
import bavteqdoit.carhealthcheck.service.EngineService;
import bavteqdoit.carhealthcheck.service.FuelService;
import bavteqdoit.carhealthcheck.service.UserService;
import org.apache.catalina.Engine;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final BrandService brandService;
    private final EngineService engineService;
    private final FuelService fuelService;

    public AdminController(UserService userService, UserRepository userRepository, BrandService brandService,  EngineService engineService,  FuelService fuelService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.brandService = brandService;
        this.engineService = engineService;
        this.fuelService = fuelService;
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

    @GetMapping("admin/brand/add")
    public String addBrand(Model model) {
        model.addAttribute("brand", new Brand());
        return "adminBrandAdd";
    }

    @PostMapping("admin/brand/add")
    public String addBrand(@ModelAttribute Brand brand) {
        brandService.save(brand);
        return "redirect:/admin/brands";
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
    public String enginesPage(Model model) {
        model.addAttribute("engines", engineService.findAll());
        return "adminEngines";
    }

    @GetMapping("/admin/engine/add")
    public String addEngineForm(Model model) {
        model.addAttribute("engine", new EngineType());
        model.addAttribute("fuels", fuelService.findAll());
        return "adminEngineAdd";
    }

    @PostMapping("/admin/engine/add")
    public String addEngine(@ModelAttribute EngineType engine) {
        engineService.save(engine);
        return "redirect:/admin/engines";
    }

    @GetMapping("/admin/engine/edit/{id}")
    public String editBrand(@PathVariable Long id, Model model) {
        EngineType engine = engineService.findById(id);
        model.addAttribute("engine", engine);
        model.addAttribute("fuels", fuelService.findAll());
        return "adminEngineEdit";
    }

    @PostMapping("/admin/engine/edit/{id}")
    public String updateEngine(@PathVariable Long id, @ModelAttribute("engine") EngineType updatedEngine) {
        EngineType engine = engineService.findById(id);

        engine.setName(updatedEngine.getName());
        engine.setCapacity(updatedEngine.getCapacity());
        engine.setFuelType(updatedEngine.getFuelType());

        engineService.save(engine);

        return "redirect:/admin/engines";
    }

    @PostMapping("/admin/engine/delete/{id}")
    public String deleteEngine(@PathVariable Long id) {
        EngineType engine = engineService.findById(id);
        if(engine.getModelTypes().isEmpty()) {
            engineService.delete(engine);
        } else {
            throw new IllegalStateException("Nie można usunąć silnika, który jest używany w modelach.");
        }
        return "redirect:/admin/engines";
    }
}