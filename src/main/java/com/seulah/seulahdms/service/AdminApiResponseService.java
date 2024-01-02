package com.seulah.seulahdms.service;

import com.seulah.seulahdms.entity.AdminApiResponse;
import com.seulah.seulahdms.entity.EligibilityQuestionSet;
import com.seulah.seulahdms.repository.AdminApiResponseRepository;
import com.seulah.seulahdms.repository.EligibilityQuestionSetRepository;
import com.seulah.seulahdms.request.AdminApiResponseRequest;
import com.seulah.seulahdms.request.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.seulah.seulahdms.utils.Constants.SUCCESS;

@Service
@Slf4j
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
            Optional<AdminApiResponse> adminApiResponseOptional = adminApiResponseRepository.findBySetId(adminApiResponseRequest.getSetId());
            AdminApiResponse adminApiResponse;
            adminApiResponse = adminApiResponseOptional.orElseGet(AdminApiResponse::new);
            return getMessageResponseResponseEntity(adminApiResponseRequest, adminApiResponse);
        }
        return new ResponseEntity<>(new MessageResponse("Invalid Set Id", null, false), HttpStatus.CREATED);
    }

    private ResponseEntity<MessageResponse> getMessageResponseResponseEntity(AdminApiResponseRequest adminApiResponseRequest, AdminApiResponse adminApiResponse) {
        if (adminApiResponseRequest.getLanguageCode() == null || adminApiResponseRequest.getLanguageCode().isEmpty()) {
            adminApiResponse.setLanguageCode("en");
        } else {
            adminApiResponse.setLanguageCode(adminApiResponseRequest.getLanguageCode());
        }
        adminApiResponse.setSetId(adminApiResponseRequest.getSetId());
        adminApiResponse.setErrorImage(adminApiResponseRequest.getErrorImage());
        adminApiResponse.setErrorDescription(adminApiResponseRequest.getErrorDescription());
        adminApiResponse.setSuccessImage(adminApiResponseRequest.getSuccessImage());
        adminApiResponse.setSuccessDescription(adminApiResponseRequest.getSuccessDescription());
        adminApiResponse.setSuccessMessage(adminApiResponseRequest.getSuccessMessage());
        adminApiResponse.setErrorMessage(adminApiResponseRequest.getErrorMessage());
        adminApiResponse = adminApiResponseRepository.save(adminApiResponse);
        log.info("Saved admin api response successfully {}", adminApiResponse);
        return new ResponseEntity<>(new MessageResponse(SUCCESS, adminApiResponse, false), HttpStatus.CREATED);
    }



    public ResponseEntity<MessageResponse> getAllResponse() {
        List<AdminApiResponse> adminApiResponseList = adminApiResponseRepository.findAll();
        log.info("Get all admin api response");
        return new ResponseEntity<>(new MessageResponse("Success", adminApiResponseList, false), HttpStatus.OK);
    }

    public ResponseEntity<MessageResponse> getAllResponseByLanguageCode(String langCode) {
        List<AdminApiResponse> adminApiResponseList = adminApiResponseRepository.findByLanguageCode(langCode);
        return new ResponseEntity<>(new MessageResponse(SUCCESS, adminApiResponseList, false), HttpStatus.OK);
    }
}
