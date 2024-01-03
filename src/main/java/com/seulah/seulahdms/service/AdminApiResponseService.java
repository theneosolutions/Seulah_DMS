package com.seulah.seulahdms.service;

import com.seulah.seulahdms.entity.AdminApiResponse;
import com.seulah.seulahdms.entity.EligibilityQuestionSet;
import com.seulah.seulahdms.repository.AdminApiResponseRepository;
import com.seulah.seulahdms.repository.EligibilityQuestionSetRepository;
import com.seulah.seulahdms.request.SuccessImageUploadRequest;
import com.seulah.seulahdms.request.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.seulah.seulahdms.utils.Constants.NO_RECORD_FOUND;
import static com.seulah.seulahdms.utils.Constants.SUCCESS;

@Service
@Slf4j
public class AdminApiResponseService {

    private final AdminApiResponseRepository adminApiResponseRepository;
    private final EligibilityQuestionSetRepository eligibilityQuestionSetRepository;
    private final  FileUploadService  fileUploadService;
    public AdminApiResponseService(AdminApiResponseRepository adminApiResponseRepository, EligibilityQuestionSetRepository eligibilityQuestionSetRepository, FileUploadService fileUploadService) {
        this.adminApiResponseRepository = adminApiResponseRepository;
        this.eligibilityQuestionSetRepository = eligibilityQuestionSetRepository;
        this.fileUploadService = fileUploadService;
    }

    public ResponseEntity<MessageResponse> successImageUpload(MultipartFile file, SuccessImageUploadRequest successImageUploadRequest) throws IOException {


        Optional<EligibilityQuestionSet> eligibilityQuestionSet = eligibilityQuestionSetRepository.findById(successImageUploadRequest.getSetId());
        if (eligibilityQuestionSet.isPresent()) {
            Optional<AdminApiResponse> adminApiResponseOptional = adminApiResponseRepository.findBySetId(successImageUploadRequest.getSetId());
            AdminApiResponse adminApiResponse;
            adminApiResponse = adminApiResponseOptional.orElseGet(AdminApiResponse::new);
            String uploadImage =  fileUploadService.uploadFile(file);
            return getMessageResponseResponseEntity(uploadImage,successImageUploadRequest, adminApiResponse);
        }
        return new ResponseEntity<>(new MessageResponse("Invalid Set Id", null, false), HttpStatus.CREATED);
    }

//    public ResponseEntity<MessageResponse> createAdminApiResponse(SuccessImageUploadRequest successImageUploadRequest) throws IOException {
//
//        Optional<EligibilityQuestionSet> eligibilityQuestionSet = eligibilityQuestionSetRepository.findById(successImageUploadRequest.getSetId());
//        if (eligibilityQuestionSet.isPresent()) {
//            Optional<AdminApiResponse> adminApiResponseOptional = adminApiResponseRepository.findBySetId(successImageUploadRequest.getSetId());
//            AdminApiResponse adminApiResponse;
//            adminApiResponse = adminApiResponseOptional.orElseGet(AdminApiResponse::new);
//            return getMessageResponseResponseEntity(successImageUploadRequest, adminApiResponse);
//        }
//        return new ResponseEntity<>(new MessageResponse("Invalid Set Id", null, false), HttpStatus.CREATED);
//    }

    private ResponseEntity<MessageResponse> getMessageResponseResponseEntity(String img ,SuccessImageUploadRequest successImageUploadRequest, AdminApiResponse adminApiResponse) {
        if (successImageUploadRequest.getLanguageCode() == null || successImageUploadRequest.getLanguageCode().isEmpty()) {
            adminApiResponse.setLanguageCode("en");
        } else {
            adminApiResponse.setLanguageCode(successImageUploadRequest.getLanguageCode());
        }
        adminApiResponse.setSetId(successImageUploadRequest.getSetId());
        adminApiResponse.setSuccessImage(img);
        adminApiResponse.setSuccessDescription(successImageUploadRequest.getSuccessDescription());
        adminApiResponse.setSuccessMessage(successImageUploadRequest.getSuccessMessage());
        adminApiResponse = adminApiResponseRepository.save(adminApiResponse);
        log.info("Saved admin api response successfully {}", adminApiResponse);
        return new ResponseEntity<>(new MessageResponse(SUCCESS, adminApiResponse, false), HttpStatus.CREATED);
    }

//    private ResponseEntity<MessageResponse> getMessageResponseResponseEntity(SuccessImageUploadRequest successImageUploadRequest, AdminApiResponse adminApiResponse) {
//        if (successImageUploadRequest.getLanguageCode() == null || successImageUploadRequest.getLanguageCode().isEmpty()) {
//            adminApiResponse.setLanguageCode("en");
//        } else {
//            adminApiResponse.setLanguageCode(successImageUploadRequest.getLanguageCode());
//        }
//        adminApiResponse.setSetId(successImageUploadRequest.getSetId());
//        adminApiResponse.setErrorImage(successImageUploadRequest.getErrorImage());
//        adminApiResponse.setErrorDescription(successImageUploadRequest.getErrorDescription());
//        adminApiResponse.setSuccessImage(successImageUploadRequest.getSuccessImage());
//        adminApiResponse.setSuccessDescription(successImageUploadRequest.getSuccessDescription());
//        adminApiResponse.setSuccessMessage(successImageUploadRequest.getSuccessMessage());
//        adminApiResponse.setErrorMessage(successImageUploadRequest.getErrorMessage());
//        adminApiResponse = adminApiResponseRepository.save(adminApiResponse);
//        log.info("Saved admin api response successfully {}", adminApiResponse);
//        return new ResponseEntity<>(new MessageResponse(SUCCESS, adminApiResponse, false), HttpStatus.CREATED);
//    }


    public ResponseEntity<MessageResponse> getAllResponse() {
        List<AdminApiResponse> adminApiResponseList = adminApiResponseRepository.findAll();
        log.info("Get all admin api response");
        return new ResponseEntity<>(new MessageResponse("Success", adminApiResponseList, false), HttpStatus.OK);
    }

    public ResponseEntity<MessageResponse> getAllResponseByLanguageCode(String langCode) {
        List<AdminApiResponse> adminApiResponseList = adminApiResponseRepository.findByLanguageCode(langCode);
        return new ResponseEntity<>(new MessageResponse(SUCCESS, adminApiResponseList, false), HttpStatus.OK);
    }

    public ResponseEntity<MessageResponse> getResponseBySetId(Long setId) {
        Optional<AdminApiResponse> adminApiResponseOptional = adminApiResponseRepository.findBySetId(setId);
        return adminApiResponseOptional.map(adminApiResponse -> new ResponseEntity<>(new MessageResponse(SUCCESS, adminApiResponse, false), HttpStatus.OK)).orElse(new ResponseEntity<>(new MessageResponse(NO_RECORD_FOUND, null, false), HttpStatus.BAD_REQUEST));
    }
}
