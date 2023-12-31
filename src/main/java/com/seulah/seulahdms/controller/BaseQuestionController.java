package com.seulah.seulahdms.controller;


import com.seulah.seulahdms.request.BaseQuestionsRequest;
import com.seulah.seulahdms.request.MessageResponse;
import com.seulah.seulahdms.service.BaseQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/baseQuestion")
public class BaseQuestionController {
    private final BaseQuestionService baseQuestionService;

    public BaseQuestionController(BaseQuestionService baseQuestionService) {
        this.baseQuestionService = baseQuestionService;
    }

    @PostMapping("/save")
    @Operation(summary = "Saving otherQuestion And numeric question")
    public ResponseEntity<MessageResponse> saveBaseQuestion(@RequestBody BaseQuestionsRequest baseQuestionsRequest){
        return baseQuestionService.saveBaseQuestion(baseQuestionsRequest);

    }
}
