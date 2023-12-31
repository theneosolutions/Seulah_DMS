package com.seulah.seulahdms.controller;

import com.seulah.seulahdms.request.MessageResponse;
import com.seulah.seulahdms.service.EligibilityQuestionSetService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/questionSet")
public class EligibilityQuestionSetController {
    private final EligibilityQuestionSetService eligibilityQuestionSetService;

    public EligibilityQuestionSetController(EligibilityQuestionSetService eligibilityQuestionSetService) {
        this.eligibilityQuestionSetService = eligibilityQuestionSetService;
    }


    @PostMapping("/saveSet")
    @Operation(summary = "Save The Question into the QuestionSet")
    public ResponseEntity<MessageResponse> saveSet(@RequestParam String setName, @RequestBody List<Long> questionIds) {
        log.info("Save New Question in Set By Question Ids ,{},{}", setName, questionIds);
        return eligibilityQuestionSetService.saveQuestionSet(setName, questionIds);
    }

    @GetMapping("/getQuestionSetById")
    @Operation(summary = "Getting QuestionSet From the database By their id")
    public ResponseEntity<MessageResponse> getQuestionSetById(@RequestParam Long id) {
        log.info("Get  Question Set By Id ,{}", id);
        return eligibilityQuestionSetService.getQuestionById(id);

    }


    @GetMapping("/getAllDecision")
    @Operation(summary = "Getting All Decision and getting Numeric and other question separately The Question into the database")
    public ResponseEntity<MessageResponse> getAllDecision() {
        log.info("Get  Question Set By Id ");
        return eligibilityQuestionSetService.getAllDecision();

    }

    @GetMapping("/getAllQuestionSet")
    @Operation(summary = "Getting All Available QuestionSet From the database")
    public ResponseEntity<MessageResponse> getQuestionSet() {
        log.info("Get All Question Sets ");
        return eligibilityQuestionSetService.getQuestions();

    }

    @GetMapping("/getQuestionSetByNumericAndString")
    @Operation(summary = "Getting QuestionSet one the base of their id and getting Numeric and other question separately The Question into the database")
    public ResponseEntity<MessageResponse> getQuestionSetByNumericAndString(@RequestParam Long id, @RequestParam Boolean forUser) {
        log.info("Get All Question Sets By Numeric And String By id {} ", id);
        if (forUser.equals(Boolean.TRUE)) {
            return eligibilityQuestionSetService.getQuestionSetByNumericAndString(id, true);
        } else {
            return eligibilityQuestionSetService.getQuestionSetByNumericAndString(id, false);
        }

    }

    @DeleteMapping("/deleteQuestionSet")
    @Operation(summary = "Deleting Question Set From the database")
    public ResponseEntity<MessageResponse> deleteQuestionSet(@RequestParam Long id) {
        log.info("Delete Question Sets,{}", id);
        return eligibilityQuestionSetService.deleteQuestion(id);
    }

    @PostMapping("/updateAnswer")
    @Operation(summary = "Update Answer into the database on the base of setId and questionId")
    public ResponseEntity<MessageResponse> updateAnswer(@RequestParam(required = false) String setName, @RequestParam Long id, @RequestParam Long questionId, @RequestBody List<String> answers) {
        log.info("Update Answer in Question Sets By Question Id ,{} and Set Id {},Answer  ,{}", questionId, id, answers);
        return eligibilityQuestionSetService.updateAnswer(setName, id, questionId, answers);
    }

    @GetMapping("/getQuestionByIdAndSetId")
    @Operation(summary = "Getting Question by their id and set id")
    public ResponseEntity<MessageResponse> getQuestionByIdAndSetId(@RequestParam Long questionId, @RequestParam Long setId) {
        log.info("Get Set Question By Question Id and Set Id ,{} ,{}", questionId, setId);
        return eligibilityQuestionSetService.getQuestionByIdAndSetId(questionId, setId);
    }

    @GetMapping("/getFormula")
    @Operation(summary = "Getting Formula exist against the setId")
    public ResponseEntity<MessageResponse> getFormulaByEligibilityQuestionSetId(@RequestParam Long eligibilityQuestionSetId) {

        return eligibilityQuestionSetService.getFormulaByEligibilityQuestionSetId(eligibilityQuestionSetId);

    }
}
