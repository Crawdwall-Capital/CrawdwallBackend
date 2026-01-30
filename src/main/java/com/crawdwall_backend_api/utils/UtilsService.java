package com.crawdwall_backend_api.utils;


import com.crawdwall_backend_api.utils.exception.ErrorProcessingRequestException;
import com.crawdwall_backend_api.utils.exception.InvalidInputException;
import com.crawdwall_backend_api.utils.exception.InvalidOperationException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class UtilsService {


    private static final Random RANDOM = new Random();

//    @Value("${application.pdfGenerator.baseUrl}")
//    private String pdfGeneratorUrl;
//    @Value("${application.pdfGenerator.apiKey}")
//    private String pdfGeneratorApiKey;

    private final RestTemplate restTemplate;
    private static final String KEEP_ALIVE_URL = "https://albion-portal-api.onrender.com/api/v1/utilities/public/stay-up";


    /**
     * This method refines the page number of a paginated request
     */
    public RefinedPagination refinePageNumber(int page, int size, long totalSize) throws IllegalArgumentException{
        if(page == 0){
            throw new InvalidInputException("Page cannot be less than 1");
        }

        if(totalSize==0){
            throw new IllegalArgumentException("Total size cannot be 0");
        }

        if(page>1) {
            int totalPages = (int) Math.ceil((double) totalSize / size);

            if (page >= totalPages) {
                // Adjust to request the last available page
                int lastPageNumber = totalPages - 1;
                return new RefinedPagination(lastPageNumber, size);
            }
        }
        return new RefinedPagination(page-1, size);
    }

    /**
     * This method converts a multipartfile to file
     */
    public static File convertMultipartFileToFile(@NotNull MultipartFile multipartFile) {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
            return file;
        }catch (IOException e){
//            log.severe(e.getMessage());
            throw new ErrorProcessingRequestException("Error Processing File, Try Again Later");
        }
    }


    /**
     * Strips HTML content from a WYSIWYG editor to plain text.
     * Removes all HTML tags, scripts, and styles, then normalizes whitespace.
     * @param html The HTML string to be processed
     * @return A plain text string with normalized whitespace
     */
    public static String stripWysiwygContent(String html) {
        // Parse the HTML
        Document doc = Jsoup.parse(html);

        // Remove script and style elements
        doc.select("script, style").forEach(Element::remove);

        // Get clean text
        String text = doc.text();

        return text.trim().replaceAll("\\s+", " ");
    }

    /**
     * Generates a random, visually appealing hex color code suitable for bright backgrounds.
     * Avoids very light and very dark colors.
     */
    public static String getRandomProfileColor() {
        // Use HSB to control vibrancy and brightness
        float hue = RANDOM.nextFloat(); // 0.0 to 1.0
        float saturation = 0.5f + RANDOM.nextFloat() * 0.5f; // 0.5 to 1.0
        float brightness = 0.5f + RANDOM.nextFloat() * 0.4f; // 0.5 to 0.9

        int rgb = java.awt.Color.HSBtoRGB(hue, saturation, brightness);
        return String.format("#%06X", (0xFFFFFF & rgb));
    }

//    public byte[] generatePdf(File file) throws IOException {
//
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//            headers.setAccept(List.of(MediaType.APPLICATION_PDF, MediaType.ALL));
//            headers.set("Authorization", "Bearer " + pdfGeneratorApiKey);
//            ;
//
//            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//            // Ensure this key matches the external API's expected part name
//            body.add("htmlFile", new FileSystemResource(file));
//
//            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//            ResponseEntity<byte[]> response = restTemplate.exchange(pdfGeneratorUrl + "/generate-pdf", HttpMethod.POST,
//                    requestEntity, byte[].class);
//
//            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
//
//                System.out.println("PDF generated successfully, size: " + response.getBody().length + " bytes");
//                return response.getBody();
//            }
//            throw new RuntimeException("Failed to generate PDF: " + response.getStatusCode());
//        } finally {
//            if (file.exists()) {
//                // noinspection ResultOfMethodCallIgnored
//                file.delete();
//            }
//        }
//    }



    /**
     * Validates a UK National Insurance number (NINO).
     *
     * <p>Behavior:
     * - Returns false for null input.
     * - Normalizes the input by removing spaces and hyphens and converting to upper case (UK locale).
     * - Validates against the standard NINO format: two prefix letters (excluding invalid combinations),
     *   six digits, and a trailing letter A-D.</p>
     *
     * @param nino the input NINO string (may contain spaces or hyphens)
     * @return true if the normalized input matches the UK NINO format, false otherwise
     */
    public boolean isValidUkNino(String nino) {
        if (nino == null) return false;

        // Normalize input
        String s = nino.replaceAll("[\\s-]+", "").toUpperCase(java.util.Locale.UK);

        // HMRC valid NI prefix letters (A-Z excluding D, F, I, Q, U, V for real NI numbers)
        // But we explicitly allow QQ because it's an official TEST number.
        //
        // 1. Allow "QQ" test prefix
        // 2. Otherwise allow real prefixes and block forbidden ones
        String regex =
                "^(?:"
                        + "QQ"                                 // allow test NI: QQ*******
                        + "|"
                        + "(?!BG)(?!GB)(?!KN)(?!NK)(?!NT)(?!TN)(?!ZZ)" // forbidden real prefixes
                        + "[A-CEGHJ-PR-TW-Z]{2}"               // allowed first two letters (real NI)
                        + ")"
                        + "\\d{6}"                                     // six digits
                        + "[A-D]$";                                    // final letter A-D

        return s.matches(regex);
    }

    public  void validateAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth is required");
        }

        LocalDate eighteenYearsAgo = LocalDate.now().minusYears(18);
        if (dateOfBirth.isAfter(eighteenYearsAgo)) {
            throw new IllegalArgumentException("Nominee must be at least 18 years old");
        }
    }

    /**
     * Validates that the issue date is either today or in the past.
     * <p>
     * The issue date is considered invalid if:
     * <ul>
     *     <li>it is tomorrow, or</li>
     *     <li>any date after tomorrow (i.e., any future date)</li>
     * </ul>
     *
     * @param issueDate the date to validate
     * @throws InvalidInputException      if issueDate is null
     * @throws InvalidOperationException  if the issue date is tomorrow or a future date
     */
    public void validateIssueDateIsTodayOrInPast(LocalDate issueDate) {
        if (issueDate == null) {
            throw new InvalidInputException("Issue date cannot be null");
        }

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        // ❌ invalid if issue date is tomorrow or any date in the future
        if (issueDate.isEqual(tomorrow) || issueDate.isAfter(tomorrow)) {
            throw new InvalidOperationException("Issue date cannot be tomorrow or any future date.");
        }
        // ✔ valid if issue date is today or in the past
    }

}



