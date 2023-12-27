package com.seulah.seulahdms.controller;

import com.seulah.seulahdms.entity.AdminApiResponse;
import com.seulah.seulahdms.request.MessageResponse;
import com.seulah.seulahdms.service.EligibilityQuestionSetService;
import com.seulah.seulahdms.service.FormulaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RequestMapping("api/v1/dms/eligibilityCheck")
public class EligibilityCheckController {


    private final FormulaService formulaService;
    private final EligibilityQuestionSetService eligibilityQuestionSetService;

    public EligibilityCheckController(FormulaService formulaService, EligibilityQuestionSetService eligibilityQuestionSetService) {
        this.formulaService = formulaService;
        this.eligibilityQuestionSetService = eligibilityQuestionSetService;
    }

    @PostMapping("/updateAndCheck")
    public ResponseEntity<MessageResponse> updateAndCheck(
            @RequestParam Long setId,
            @RequestParam String userId,
            @RequestBody List<Map<String, Object>> userInputList) {

        ResponseEntity<MessageResponse> updateResponse = eligibilityQuestionSetService.updateUserAnswer(setId, userInputList);
        if (updateResponse.getStatusCode() != HttpStatus.OK) {
            return updateResponse;
        }

        ResponseEntity<MessageResponse> formulaResponse = formulaService.calculateFormula(setId, userInputList, userId);
        if (formulaResponse.getStatusCode() != HttpStatus.OK) {
            return formulaResponse;
        }

        ResponseEntity<AdminApiResponse> eligibilityResponse = eligibilityQuestionSetService.checkEligibility(setId, userId);
        ResponseEntity<MessageResponse> userEligibility = formulaService.userEligibility(userId);
        HashMap<String, Object> response = new HashMap<>();
        response.put("Eligibility Response", eligibilityResponse);
        response.put("User Eligibility", userEligibility);

        return new ResponseEntity<>(new MessageResponse("Combined Operation Successful", response, false), HttpStatus.OK);
    }


}