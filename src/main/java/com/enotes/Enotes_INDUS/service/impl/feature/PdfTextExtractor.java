package com.enotes.Enotes_INDUS.service.impl.feature;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class PdfTextExtractor{


    public String extract(MultipartFile file) {
        try {

            String pdfBoxText = extractWithPdfBox(file);

            if (pdfBoxText != null && !pdfBoxText.trim().isEmpty()) {
                log.info("PDF extracted using PDFBox");
                return pdfBoxText;
            }

            // 2️⃣ Fallback to Tika
            Tika tika = new Tika();
            String tikaText = tika.parseToString(file.getInputStream());

            if (tikaText != null && !tikaText.trim().isEmpty()) {
                log.info("PDF extracted using Tika");
                return tikaText;
            }

            // 3️⃣ Final fallback (optional OCR later)
            log.warn("PDF text empty after PDFBox + Tika");
            return "";

        } catch (Exception e) {
            throw new RuntimeException("PDF extraction failed", e);
        }
    }


    private String extractWithPdfBox(MultipartFile file) throws Exception {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            return stripper.getText(document);
        }
    }
}
