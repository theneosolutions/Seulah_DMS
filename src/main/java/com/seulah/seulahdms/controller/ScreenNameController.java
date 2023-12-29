package com.seulah.seulahdms.controller;

import com.seulah.seulahdms.request.MessageResponse;
import com.seulah.seulahdms.request.ScreenNameRequest;
import com.seulah.seulahdms.service.ScreenNameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Muhammad Mansoor
 */
@RestController
@RequestMapping("api/v1/dms/screenName")
@Slf4j
public class ScreenNameController {
    private final ScreenNameService screenNameService;

    public ScreenNameController(ScreenNameService screenNameService) {
        this.screenNameService = screenNameService;
    }

    @PostMapping("addQuestionAgainstScreenName")
    public ResponseEntity<MessageResponse> addQuestionAgainstScreenName(@RequestBody ScreenNameRequest screenNameRequest) {
        log.info("Adding question against screenName {}", screenNameRequest);
        return screenNameService.addQuestionAgainstScreenName(screenNameRequest);
    }
}
