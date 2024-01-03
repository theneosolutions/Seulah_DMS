package com.seulah.seulahdms.controller;

import com.seulah.seulahdms.request.SuccessImageUploadRequest;
import com.seulah.seulahdms.request.MessageResponse;
import com.seulah.seulahdms.service.AdminApiResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/dms/apiResponse")
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001","http://localhost:8085"}, maxAge = 3600, allowCredentials = "true")
public class AdminApiResponseController {

    private final AdminApiResponseService adminApiResponseService;

    public AdminApiResponseController(AdminApiResponseService adminApiResponseService) {
        this.adminApiResponseService = adminApiResponseService;
    }

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createAdminApiResponse(@RequestBody SuccessImageUploadRequest successImageUploadRequest) throws IOException {
        log.info("Creating new admin api response");
        return adminApiResponseService.createAdminApiResponse(successImageUploadRequest);

    }

    @PostMapping("/uploadSuccessImage")
    public ResponseEntity<MessageResponse> createSuccessImage(@RequestParam("successImage") MultipartFile file,@RequestParam String successMessage, @RequestParam String successDescription, @RequestParam Long setId,@RequestParam String languageCode) throws IOException {
        log.info("Upload Success Image");
        SuccessImageUploadRequest successImageUploadRequest = new SuccessImageUploadRequest();
        successImageUploadRequest.setSuccessMessage(successMessage);
        successImageUploadRequest.setSuccessDescription(successDescription);
        successImageUploadRequest.setSetId(setId);
        successImageUploadRequest.setLanguageCode(languageCode);
        return adminApiResponseService.successImageUpload(file, successImageUploadRequest);

    }
//    @PostMapping("/createErrorImage")
//    public ResponseEntity<MessageResponse> createErrorImage(@RequestParam("file") MultipartFile file, @RequestBody SuccessImageUploadRequest successImageUploadRequest) throws IOException {
//        log.info("Creating new admin api response");
//        return adminApiResponseService.createAdminApiResponse(file, successImageUploadRequest);
//
//    }
    @GetMapping("/getAllResponse")
    public ResponseEntity<MessageResponse> getAllResponse() {
        log.info("Getting admin api response");
        return adminApiResponseService.getAllResponse();

    }

    @GetMapping("/getAllResponseByLanguageCode")
    public ResponseEntity<MessageResponse> getAllResponseByLanguageCode(@RequestParam String langCode) {
        log.info("Getting admin api response by language code ,{}", langCode);
        return adminApiResponseService.getAllResponseByLanguageCode(langCode);

    }
    @GetMapping("/getResponseBySetId")
    public ResponseEntity<MessageResponse> getResponseBySetId(@RequestParam Long setId) {
        log.info("Getting admin api response by set id ,{}", setId);
        return adminApiResponseService.getResponseBySetId(setId);
    }
}
