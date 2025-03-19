package bavteqdoit.carhealthcheck.web;

import bavteqdoit.carhealthcheck.Owner;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/cars")
public class OwnerController {

    @GetMapping("/current")
    public String ownerForm(Model model) {
        model.addAttribute("owner",new Owner());
        return "ownerForm";
    }

    @PostMapping
    public String processOwner(@Valid Owner owner, Errors errors) {
        if (errors.hasErrors()) {
            return "ownerForm";
        }
        log.info("Processing owner" + owner);
        return "redirect:/";
    }
}
