package com.crawdwall_backend_api.utils.fileupload;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.crawdwall_backend_api.utils.ApiResponseMessages;
import com.crawdwall_backend_api.utils.UtilsService;
import com.crawdwall_backend_api.utils.exception.ErrorProcessingRequestException;
import com.crawdwall_backend_api.utils.fileupload.response.FileUploadResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class CloudinaryFileUploadServiceImpl implements FileUploadService {

    @Value("${application.cloudinary.cloud-name}")
    private String cloudName;
    @Value("${application.cloudinary.api-key}")
    private String apiKey;
    @Value("${application.cloudinary.api-secret}")
    private String apiSecret;
    private Cloudinary cloudinary;

    private final UtilsService utilsService;

    @PostConstruct
    private void initializeCloudinary() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    /**
     * Uploads a file to a specified storage location based on the provided category and extra data.
     * Depending on the file category (e.g., EMPLOYEE_PROFILE_IMAGE or EMPLOYEE_DOCUMENT), the file path is
     * dynamically generated using extra data like `clientId` and `employeeId`. The file is then uploaded to the
     * storage service.
     *
     * @param file      the file to be uploaded.
     * @param category  the category of the file, which determines its storage path.
     * @param extraData additional data required to generate the file path (e.g., ).
     * @return a {@link FileUploadResponse} containing the file's URL and public ID after a successful upload.
     * @throws ErrorProcessingRequestException if the file category is invalid or the upload fails.
     */
    @Override
    public FileUploadResponse uploadFile(MultipartFile file, FileUploadCategory category, Map<String, String> extraData) {

        String filePath;
        switch (category) {
            case PROFILE_IMAGE ->
                    filePath = String.format("image/profileImage_%s/image", extraData.get("userId"));
            case COMPANY_LOGO ->
                    filePath = String.format("image/company-logo/userId_%s/image", extraData.get("userId"));
            case ADMIN ->
                    filePath = String.format("document/userId_%s/document", extraData.get("userId"));
            default ->
                    throw new ErrorProcessingRequestException(ApiResponseMessages.ERROR_PROCESSING_REQUEST_INVALID_FILE_CATEGORY);
        }
        ;
        return uploadFile(file, filePath, "auto");
    }

    /**
     * Deletes a file from the storage service using its URL.
     * Extracts the public ID of the file from the given URL and deletes the file from the storage provider.
     *
     * @param fileUrl the URL of the file to be deleted.
     * @throws ErrorProcessingRequestException if the file deletion fails or the file URL is invalid.
     */
    @Override
    public void deleteFile(String fileUrl) {
        try {
            if (fileUrl != null) {
                String filePublicId = getPublicIdFromUrl(fileUrl);

                Map result = cloudinary.uploader().destroy(filePublicId, ObjectUtils.emptyMap());
            }
        } catch (Exception e) {
            throw new ErrorProcessingRequestException("Error Deleting File, Try Again Later");
        }
    }

    @Override
    @Async
    public void deleteFileAsync(String fileUrl) {
        try {
            if (fileUrl != null) {
                String filePublicId = getPublicIdFromUrl(fileUrl);

                Map result = cloudinary.uploader().destroy(filePublicId, ObjectUtils.emptyMap());
            }
        } catch (Exception e) {
            throw new ErrorProcessingRequestException("Error Deleting File, Try Again Later");
        }
    }

    @Override
    @Async
    public void deleteFileAsync(Collection<String> fileUrls) {
        for (String fileUrl : fileUrls) {
            try {
                if (fileUrl != null) {
                    String filePublicId = getPublicIdFromUrl(fileUrl);
                    cloudinary.uploader().destroy(filePublicId, ObjectUtils.emptyMap());
                }
            } catch (Exception e) {
                log.error("Error Deleting File: {}", fileUrl, e);
                continue;
            }
        }
    }


    /**
     * Uploads a file to a cloud storage provider with specific configurations.
     * <p>
     * This method converts the provided {@link MultipartFile} to a standard {@link File}, configures the upload
     * options such as the folder path and resource type, and uploads the file to the cloud storage service. After
     * the upload is completed, the temporary file is deleted from the server to ensure proper cleanup.
     *
     * @param multipartFile the multipart file to be uploaded.
     * @param filePath      the folder path where the file will be stored in the cloud.
     * @param resourceType  the type of the resource (e.g., "image", "video"). If null, the default resource type is used.
     * @return a {@link FileUploadResponse} containing the URL and public ID of the uploaded file.
     * @throws ErrorProcessingRequestException if the file upload fails or any exception occurs during the process.
     */
    private FileUploadResponse uploadFile(MultipartFile multipartFile, String filePath, String resourceType) {

        File file = UtilsService.convertMultipartFileToFile(multipartFile);
        try {

            Map<String, String> options = new HashMap<>();
            options.put("folder", filePath);
            options.put("access_mode", "public");
            if (resourceType != null) {
                options.put("resource_type", resourceType);
            }

            // Upload file with options
            Map uploadResult = cloudinary.uploader().upload(file, options);

            return new FileUploadResponse(
                    (String) uploadResult.get("secure_url"),
                    (String) uploadResult.get("public_id")
            );
        } catch (Exception e) {
            throw new ErrorProcessingRequestException("Error Uploading File, Try Again Later");
        } finally {
            file.delete(); //delete the file from the server
        }
    }

    /**
     * Extracts the public ID from a file URL, which is typically used in file storage or media management systems.
     * The method assumes the URL contains a "/upload/" segment, followed by a version identifier (optional)
     * and the actual public ID with a file extension. It parses and removes the version identifier and the file
     * extension to extract the public ID.
     * Example:
     * Input URL: "https://example.com/upload/v1234/folder/file.png"
     * Output: "folder/file"
     *
     * @param url the full URL of the file (e.g., from a cloud storage provider).
     * @return the extracted public ID without the version number or file extension.
     * @throws ErrorProcessingRequestException if the URL does not contain the "/upload/" segment or is malformed.
     */
    private static String getPublicIdFromUrl(String url) {
        // Split the URL on "/upload/"
        String[] parts = url.split("/upload/");

        if (parts.length > 1) {
            // Get the part after "/upload/"
            String publicIdWithVersion = parts[1];

            // Remove the version number if present (it starts with 'v' followed by digits)
            // Extract everything after the version number and before the file extension
            String publicIdWithExtension = publicIdWithVersion.substring(publicIdWithVersion.indexOf('/', 2) + 1);

            // Remove the file extension (e.g., ".png", ".jpg")
            return publicIdWithExtension.substring(0, publicIdWithExtension.lastIndexOf('.'));
        }

        throw new ErrorProcessingRequestException("Error Processing File, Try Again Later");
    }




}
