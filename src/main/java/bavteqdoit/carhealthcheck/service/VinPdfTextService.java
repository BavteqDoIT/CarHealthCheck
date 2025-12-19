package bavteqdoit.carhealthcheck.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
public class VinPdfTextService {

    public String extractText(byte[] pdfBytes) {
        try(PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))){
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (Exception e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
