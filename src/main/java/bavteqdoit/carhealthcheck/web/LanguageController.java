package bavteqdoit.carhealthcheck.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Controller
public class LanguageController {

    private final LocaleResolver localeResolver;

    public LanguageController(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @GetMapping("/change-lang")
    public String changeLanguage(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestParam String lang) {
        Locale locale = switch (lang) {
            case "pl" -> new Locale("pl");
            case "de" -> new Locale("de");
            default -> Locale.ENGLISH;
        };
        localeResolver.setLocale(request, response, locale);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}
