package com.crawdwall_backend_api.utils;



import com.crawdwall_backend_api.utils.fileupload.FileUploadCategory;
import com.crawdwall_backend_api.utils.fileupload.FileUploadService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/utilities")
@AllArgsConstructor
public class UtilityController {

    private final FileUploadService fileUploadService;

    @PostMapping("/public/file")
    ResponseEntity <ApiResponse> uploadFile(@RequestParam(name = "file") MultipartFile file,
                                            @RequestParam(name = "category") FileUploadCategory category,
                                            @RequestParam(value = "nomineeId", required = false) String nomineeId) {

        Map<String, String> extraData = Map.of(
                "nomineeId", nomineeId != null ? nomineeId : ""
        );

        return ResponseEntity.ok(new ApiResponse(true, "File Uploaded Successfully",
                fileUploadService.uploadFile(file,category,extraData)));
    }





}
