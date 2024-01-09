package com.seulah.seulahdms.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.seulah.seulahdms.request.MessageResponse;
import com.seulah.seulahdms.request.ScreenRequest;
import com.seulah.seulahdms.response.CustomFinalScreenResponse;
import com.seulah.seulahdms.service.ScreenService;
import com.seulah.seulahdms.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/dms/screen")
@Slf4j
public class ScreenController {
    private final ScreenService screenService;
    public ScreenController(ScreenService screenService) {
        this.screenService = screenService;
    }

    @PostMapping("/addQuestion")
    public ResponseEntity<MessageResponse> addScreen(@RequestBody ScreenRequest screenRequest) {
        log.info("Adding Screen and Questions {}", screenRequest);
        return screenService.addScreen(screenRequest);
    }

    @GetMapping("/getScreen")
    public ResponseEntity<?> getScreen(@RequestParam String screenHeading) {
        log.info("Get Screen {}", screenHeading);
        return ResponseEntity.ok().body(screenService.getScreen());
    }

    @GetMapping("/questionCheckInScreen")
    public ResponseEntity<?> questionCheckInScreen(@RequestParam String questionId) {
        log.info("Question Check In Screen {}", questionId);
        return screenService.getQuestionCheck(questionId);
    }

    @GetMapping("/getScreenBySetId")
    public ResponseEntity<MessageResponse> getScreenBySetId(@RequestParam Long setId) {
        log.info("Getting all screen by set id {}", setId);
        return screenService.getScreenBySetId(setId);
    }

    @GetMapping("/getScreenWithQuestionDetailBySetId")
    public List<CustomFinalScreenResponse>getScreenWithQuestionDetailBySetId(@RequestParam Long setId) throws JsonProcessingException {
        log.info("Getting screen with question detail by set id {}", setId);
        return screenService.getScreenWithQuestionDetailBySetId(setId);
    }

    @GetMapping("/getAllScreenWithQuestionDetail")
    public List<CustomFinalScreenResponse> getAllScreenWithQuestionDetail() {
        log.info("Getting all screen with question detail ");
        return screenService.getAllScreenWithQuestionDetail();
    }

}
