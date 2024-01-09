package com.seulah.seulahdms.controller;

import com.seulah.seulahdms.request.AdminApiResponseRequest;
import com.seulah.seulahdms.request.MessageResponse;
import com.seulah.seulahdms.service.AdminApiResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/dms/apiResponse")
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001","https://main.d2j34yk77rmups.amplifyapp.com/","http://localhost:8085"}, maxAge = 3600, allowCredentials = "true")
public class AdminApiResponseController {

    private final AdminApiResponseService adminApiResponseService;

    public AdminApiResponseController(AdminApiResponseService adminApiResponseService) {
        this.adminApiResponseService = adminApiResponseService;
    }

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createAdminApiResponse(@RequestBody AdminApiResponseRequest adminApiResponseRequest) {
        log.info("Creating new admin api response");
        return adminApiResponseService.createAdminApiResponse(adminApiResponseRequest);

    }

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
