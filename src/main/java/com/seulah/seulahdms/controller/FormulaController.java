package com.seulah.seulahdms.controller;

import com.seulah.seulahdms.request.FormulaRequest;
import com.seulah.seulahdms.request.MessageResponse;
import com.seulah.seulahdms.service.FormulaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/formula")
public class FormulaController {

    private final FormulaService formulaService;

    public FormulaController(FormulaService formulaService) {
        this.formulaService = formulaService;
    }


    @PostMapping("create")
    @Operation(summary = "Create Formula Against Set ID")
    public ResponseEntity<MessageResponse> createFormula(@RequestBody FormulaRequest formulaRequest, @RequestParam Long setId) {
        return formulaService.createCalculation(formulaRequest,setId);

    }

    @GetMapping("get")
    @Operation(summary = "Getting All Formulas")
    public ResponseEntity<MessageResponse> getFormula( ) {
        return formulaService.getFormula();

    }

    @DeleteMapping("delete")
    @Operation(summary = "Delete Formula By their id")
    public ResponseEntity<MessageResponse> deleteFormulaBySetId(@RequestParam Long setId ) {
        return formulaService.deleteFormulaBySetId(setId);

    }

    @PostMapping("calculateFormula")
    @Operation(summary = "Getting Calculation by providing setId and answer")
    public ResponseEntity<MessageResponse> calculateFormula(@RequestParam Long setId,@RequestBody List<Map<String,Double>> userInput ) {
        return formulaService.calculateFormula(setId,userInput);

    }


}

