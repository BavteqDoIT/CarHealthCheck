package bavteqdoit.carhealthcheck.web;

import bavteqdoit.carhealthcheck.data.UserRepository;
import bavteqdoit.carhealthcheck.dto.EngineTypeForm;
import bavteqdoit.carhealthcheck.model.*;
import bavteqdoit.carhealthcheck.service.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class AdminController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final BrandService brandService;
    private final EngineService engineService;
    private final FuelService fuelService;
    private final ModelService modelService;

    public AdminController(UserService userService, UserRepository userRepository, BrandService brandService,  EngineService engineService,  FuelService fuelService, ModelService modelService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.brandService = brandService;
        this.engineService = engineService;
        this.fuelService = fuelService;
        this.modelService = modelService;
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
    public String addBrand(@ModelAttribute Brand brand, RedirectAttributes redirectAttributes, Model model) {
        try {
            brandService.add(brand);
            redirectAttributes.addFlashAttribute("successMessage", "admin.addBrand.success");
            return "redirect:/admin/brands";
        } catch (EntityExistsException e) {
            model.addAttribute("errorMessage", "admin.addBrand.error");
            return "adminBrandAdd";
        }
    }

    @GetMapping("/admin/brands/edit/{id}")
    public String editBrandForm(@PathVariable Long id, Model model) {
        Brand brand = brandService.findById(id);
        model.addAttribute("brand", brand);
        return "adminBrandEdit";
    }

    @PostMapping("/admin/brands/edit/{id}")
    public String updateBrand(@PathVariable Long id,
                              @ModelAttribute("brand") Brand brand,
                              Model model) {
        try {
            brand.setId(id);
            brandService.update(brand);
            return "redirect:/admin/brands";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "adminBrandEdit";
        }
    }

    @PostMapping("/admin/brands/delete/{id}")
    public String deleteBrand(@PathVariable Long id) {
        brandService.deleteById(id);
        return "redirect:/admin/brands";
    }

    @GetMapping("admin/models")
    public String modelsPage(
            @RequestParam(defaultValue = "brand.brandName") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            Model model) {

        model.addAttribute("models", modelService.findAllSorted(sortField, sortOrder));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("reverseSortOrder", sortOrder.equals("asc") ? "desc" : "asc");
        return "adminModels"; }

    @PostMapping("/admin/model/delete/{id}")
    public String deleteModel(@PathVariable Long id) {
        modelService.deleteById(id);
        return "redirect:/admin/models";
    }

    @GetMapping("/admin/model/edit/{id}")
    public String editModel(@PathVariable Long id, Model model) {
        model.addAttribute("model", modelService.findById(id));
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("engines", engineService.findAll());
        return "adminModelEdit";
    }

    @PostMapping("/admin/model/edit/{id}")
    public String editModel(@PathVariable Long id, @ModelAttribute("model") ModelType updatedModel) {
        modelService.updateModel(id, updatedModel);
        return "redirect:/admin/models";
    }

    @GetMapping("/admin/model/add")
    public String addModel( Model model) {
        model.addAttribute("model", new ModelType());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("engines", engineService.findAll());
        return "adminModelAdd";
    }

    @PostMapping("/admin/model/add")
    public String addModel(@ModelAttribute ModelType model) {
        modelService.addModel(model);
        return "redirect:/admin/models";
    }

    @GetMapping("admin/engines")
    public String enginesPage(Model model) {
        model.addAttribute("engines", engineService.findAll());
        model.addAttribute("brands",  brandService.findAll());
        return "adminEngines";
    }

    @GetMapping("/admin/engine/add")
    public String addEngineForm(Model model) {
        model.addAttribute("engine", new EngineTypeForm());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("fuels", fuelService.findAll());
        return "adminEngineAdd";
    }

    @PostMapping("/admin/engine/add")
    public String addEngine(@Valid @ModelAttribute("engine") EngineTypeForm form,
                            BindingResult br, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("brands", brandService.findAll());
            model.addAttribute("fuels", fuelService.findAll());
            return "adminEngineAdd";
        }

        EngineType e = new EngineType();
        mapFormToEntity(form, e);

        engineService.save(e);
        return "redirect:/admin/engines";
    }

    @GetMapping("/admin/engine/edit/{id}")
    public String editEngineForm(@PathVariable Long id, Model model) {
        EngineType engine = engineService.findById(id);

        EngineTypeForm form = new EngineTypeForm();
        form.setId(engine.getId());
        form.setBrandId(engine.getBrand().getId());
        form.setFuelTypeId(engine.getFuelType().getId());
        form.setName(engine.getName());
        form.setCapacity(engine.getCapacity());
        form.setHorsepowerHp(engine.getHorsepowerHp());
        form.setPowerKw(engine.getPowerKw());

        model.addAttribute("engine", form);
        model.addAttribute("fuels", fuelService.findAll());
        return "adminEngineEdit";
    }


    @PostMapping("/admin/engine/edit")
    public String editEngine(@Valid @ModelAttribute("engine") EngineTypeForm form,
                             BindingResult br, Model model) {

        if (br.hasErrors()) {
            model.addAttribute("fuels", fuelService.findAll());
            return "adminEngineEdit";
        }

        EngineType e = engineService.findById(form.getId());
        mapFormToEntity(form, e);
        engineService.save(e);

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

    private void mapFormToEntity(EngineTypeForm form, EngineType e) {
        e.setBrand(brandService.findById(form.getBrandId()));
        e.setFuelType(fuelService.findById(form.getFuelTypeId()));
        e.setName(form.getName());
        e.setCapacity(form.getCapacity());
        e.setHorsepowerHp(form.getHorsepowerHp());
        e.setPowerKw(form.getPowerKw());
    }
}