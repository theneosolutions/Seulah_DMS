package com.seulah.seulahdms.controller;


import com.seulah.seulahdms.repository.QuestionSetRepository;
import com.seulah.seulahdms.request.EligibilityQuestionsRequest;
import com.seulah.seulahdms.request.MessageResponse;
import com.seulah.seulahdms.service.EligibilityQuestionsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/dms/eligibilityQuestions")
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001","http://localhost:8085"}, maxAge = 3600, allowCredentials = "true")
public class EligibilityQuestionsController {

    private final EligibilityQuestionsService eligibilityQuestionsService;

    public final QuestionSetRepository questionsSetRepo;

    public EligibilityQuestionsController(EligibilityQuestionsService eligibilityQuestionsService, QuestionSetRepository questionsSetRepo) {
        this.eligibilityQuestionsService = eligibilityQuestionsService;
        this.questionsSetRepo = questionsSetRepo;
    }


    @PostMapping("/save-question")
    @Operation(summary = "Save The Question into the database")
    public ResponseEntity<MessageResponse> saveQuestion(@RequestBody EligibilityQuestionsRequest eligibilityQuestionsRequest) {
        log.info("Save New Eligibility Question ,{}",eligibilityQuestionsRequest);
        return eligibilityQuestionsService.saveQuestion(eligibilityQuestionsRequest);
    }

    @PostMapping("/updateQuestion")
    @Operation(summary = "Update The Question into the database on base of their id's")
    public ResponseEntity<MessageResponse> updateQuestion(@RequestBody EligibilityQuestionsRequest eligibilityQuestionsRequest, @RequestParam Long id) {
        log.info("Update Eligibility Question, {} by id ,{}",eligibilityQuestionsRequest,id);
        return eligibilityQuestionsService.updateQuestion(eligibilityQuestionsRequest, id);
    }


    @GetMapping("/getAllQuestions")
    @Operation(summary = "Getting All The Question From the database")
    public ResponseEntity<MessageResponse> getQuestion() {
        log.info("Get All Eligibility Question ");
        return eligibilityQuestionsService.getQuestions();

    }

    @GetMapping("/getQuestionById")
    @Operation(summary = "Get Question From the database By their id")
    public ResponseEntity<MessageResponse> getQuestionById(@RequestParam Long id) {
        log.info("Get Eligibility Question By Id ,{}",id);
        return eligibilityQuestionsService.getQuestionById(id);
    }

    @DeleteMapping("/delete-question")
    @Operation(summary = "Delete Question from the database by their id")
    public ResponseEntity<MessageResponse> deleteQuestion(@RequestParam Long id) {
        log.info("Delete Eligibility Question By Id ,{}",id);
        return eligibilityQuestionsService.deleteQuestion(id);
    }

}
