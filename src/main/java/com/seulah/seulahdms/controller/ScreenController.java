package com.seulah.seulahdms.controller;


import com.seulah.seulahdms.request.MessageResponse;
import com.seulah.seulahdms.request.ScreenRequest;
import com.seulah.seulahdms.service.ScreenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/dms/screen")
@Slf4j
public class ScreenController {
    private final ScreenService screenService;

    public ScreenController(ScreenService screenService) {
        this.screenService = screenService;
    }

    @PostMapping("/addQuestion")
    public ResponseEntity<?> addScreen(@RequestBody ScreenRequest screenRequest) {
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
}
