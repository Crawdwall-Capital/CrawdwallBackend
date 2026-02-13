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

    @PostMapping("/public/upload")
    ResponseEntity <ApiResponse> uploadFile(@RequestParam(name = "file") MultipartFile file,
                                            @RequestParam(name = "category") FileUploadCategory category,
                                            @RequestParam(value = "userId", required = false) String userId
                                            ) {

        Map<String, String> extraData = Map.of(
                "userId", userId != null ? userId : ""
        );

        return ResponseEntity.ok(new ApiResponse(true, "File Uploaded Successfully",
                fileUploadService.uploadFile(file,category,extraData)));
    }

    @GetMapping("/public/stay-up")
    ResponseEntity<ApiResponse> stayUp() {
        return ResponseEntity.ok(new ApiResponse(true, "Server is awake.😎😎😎😎😎😎", null));
    }





}
