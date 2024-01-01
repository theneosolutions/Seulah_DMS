package com.seulah.seulahdms.controller;

import com.seulah.seulahdms.entity.UserVerifiedType;
import com.seulah.seulahdms.request.FormulaRequest;
import com.seulah.seulahdms.request.MessageResponse;
import com.seulah.seulahdms.service.FormulaService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/dms/formula")
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001","http://localhost:8085"}, maxAge = 3600, allowCredentials = "true")
public class FormulaController {

    private final FormulaService formulaService;

    public FormulaController(FormulaService formulaService) {
        this.formulaService = formulaService;
    }


    @PostMapping("create")
    @Operation(summary = "Create Formula Against Set ID")
    public ResponseEntity<MessageResponse> createFormula(@RequestBody FormulaRequest formulaRequest, @RequestParam Long setId) {
        log.info("Creating formula {}", formulaRequest);
        return formulaService.createCalculation(formulaRequest, setId);

    }

    @GetMapping("get")
    @Operation(summary = "Getting All Formulas")
    public ResponseEntity<MessageResponse> getFormula() {
        log.info("Getting all formula");
        return formulaService.getFormula();

    }

    @DeleteMapping("delete")
    @Operation(summary = "Delete Formula By their id")
    public ResponseEntity<MessageResponse> deleteFormulaBySetId(@RequestParam Long setId) {
        log.info("Deleting formula by id");
        return formulaService.deleteFormulaBySetId(setId);

    }

    @PostMapping("calculateFormula")
    @Operation(summary = "Getting Calculation by providing setId and answer")
    public ResponseEntity<MessageResponse> calculateFormula(@RequestParam Long setId, @RequestBody List<Map<String, Object>> userInputList, @RequestParam String userId) {
        return formulaService.calculateFormula(setId, userInputList, userId);

    }

    @GetMapping("checkUserEligibility")
    public ResponseEntity<MessageResponse> userEligibilityByUserId(@RequestParam String userId) {
        log.info("checking user eligibility by user id :{}", userId);
        return formulaService.userEligibility(userId);

    }

    @GetMapping("checkAllUserEligibility")
    public ResponseEntity<MessageResponse> checkAllUserEligibility(@RequestParam UserVerifiedType userVerifiedType) {
        log.info("Getting all user eligibility by there type :{}", userVerifiedType);
        return formulaService.checkAllUserEligibility(userVerifiedType);

    }


}

