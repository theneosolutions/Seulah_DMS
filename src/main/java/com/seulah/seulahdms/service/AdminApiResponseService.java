package com.seulah.seulahdms.service;

import com.seulah.seulahdms.entity.AdminApiResponse;
import com.seulah.seulahdms.entity.EligibilityQuestionSet;
import com.seulah.seulahdms.repository.AdminApiResponseRepository;
import com.seulah.seulahdms.repository.EligibilityQuestionSetRepository;
import com.seulah.seulahdms.request.AdminApiResponseRequest;
import com.seulah.seulahdms.request.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminApiResponseService {

    private final AdminApiResponseRepository adminApiResponseRepository;
    private final EligibilityQuestionSetRepository eligibilityQuestionSetRepository;

    public AdminApiResponseService(AdminApiResponseRepository adminApiResponseRepository, EligibilityQuestionSetRepository eligibilityQuestionSetRepository) {
        this.adminApiResponseRepository = adminApiResponseRepository;
        this.eligibilityQuestionSetRepository = eligibilityQuestionSetRepository;
    }

    public ResponseEntity<MessageResponse> createAdminApiResponse(AdminApiResponseRequest adminApiResponseRequest) {
        Optional<EligibilityQuestionSet> eligibilityQuestionSet = eligibilityQuestionSetRepository.findById(adminApiResponseRequest.getSetId());
        if (eligibilityQuestionSet.isPresent()) {
            AdminApiResponse adminApiResponse = getAdminApiResponse(adminApiResponseRequest);
            adminApiResponse = adminApiResponseRepository.save(adminApiResponse);
            return new ResponseEntity<>(new MessageResponse("Success", adminApiResponse, false), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new MessageResponse("Invalid Set Id", null, false), HttpStatus.CREATED);
    }

    private static AdminApiResponse getAdminApiResponse(AdminApiResponseRequest adminApiResponseRequest) {
        AdminApiResponse adminApiResponse = new AdminApiResponse();
        adminApiResponse.setSetId(adminApiResponseRequest.getSetId());
        adminApiResponse.setErrorImage(adminApiResponseRequest.getErrorImage());
        adminApiResponse.setErrorDescription(adminApiResponseRequest.getErrorDescription());
        adminApiResponse.setSuccessImage(adminApiResponseRequest.getSuccessImage());
        adminApiResponse.setSuccessDescription(adminApiResponseRequest.getSuccessDescription());
        adminApiResponse.setSuccessMessage(adminApiResponseRequest.getSuccessMessage());
        adminApiResponse.setErrorMessage(adminApiResponseRequest.getErrorMessage());
        return adminApiResponse;
    }
}
