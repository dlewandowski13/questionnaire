package com.s26462.questionnaire.questionnaire.pdfgenerator;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.s26462.questionnaire.exception.FailedToGeneratePdfException;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.s26462.questionnaire.questionnaire.dto.QuestionnaireWithProductsDto;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
@Component
public class PdfGenerator {
    public static byte[] generatePdfFromQuestionnaire(QuestionnaireWithProductsDto questionnaireWithProductsDto) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(baos));
            Document document = new Document(pdfDocument);

            PdfFont font = PdfFontFactory.createFont("src/main/resources/fonts/arial.ttf", PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);

            Field[] fields = QuestionnaireWithProductsDto.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(questionnaireWithProductsDto);
                String fieldName = field.getName();
                String fieldValue = value != null ? value.toString() : "";

                document.add(new Paragraph(fieldName + ": " + fieldValue).setFont(font));
            }

            document.close();

            return baos.toByteArray();
        } catch (Exception e) {
            throw new FailedToGeneratePdfException(String.format("Nieudana próba wygenerowania pdf dla ankiety o id %s",
                    questionnaireWithProductsDto.getId()));
        }
    }
}
