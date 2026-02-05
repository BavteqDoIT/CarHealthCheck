package bavteqdoit.carhealthcheck.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final SpringTemplateEngine templateEngine;

    public byte[] renderTemplateToPdf(String template, Map<String, Object> model, Locale locale) {
        Context ctx = new Context(locale);
        model.forEach(ctx::setVariable);

        String html = templateEngine.process(template, ctx);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);

            builder.useFont(
                    () -> getClass().getResourceAsStream("/fonts/DejaVuSans.ttf"),
                    "DejaVu Sans"
            );

            builder.toStream(out);
            builder.run();
            return out.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("Nie udało się wygenerować PDF", e);
        }
    }
}
