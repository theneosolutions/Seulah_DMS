package com.seulah.seulahdms.controller;

import com.seulah.seulahdms.entity.AdminApiResponse;
import com.seulah.seulahdms.request.AdminApiResponseRequest;
import com.seulah.seulahdms.request.FormulaRequest;
import com.seulah.seulahdms.request.MessageResponse;
import com.seulah.seulahdms.service.AdminApiResponseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("apiResponse")
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
