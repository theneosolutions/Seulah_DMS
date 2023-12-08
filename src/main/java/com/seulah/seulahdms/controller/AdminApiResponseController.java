package com.seulah.seulahdms.controller;

import com.seulah.seulahdms.request.AdminApiResponseRequest;
import com.seulah.seulahdms.request.MessageResponse;
import com.seulah.seulahdms.service.AdminApiResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/dms/apiResponse")
@Slf4j
public class AdminApiResponseController {

    private final AdminApiResponseService adminApiResponseService;

    public AdminApiResponseController(AdminApiResponseService adminApiResponseService) {
        this.adminApiResponseService = adminApiResponseService;
    }

    @PostMapping("create")
    public ResponseEntity<MessageResponse> createAdminApiResponse(@RequestBody AdminApiResponseRequest adminApiResponseRequest) {
        log.info("Creating new admin api response");
        return adminApiResponseService.createAdminApiResponse(adminApiResponseRequest);

    }
}
