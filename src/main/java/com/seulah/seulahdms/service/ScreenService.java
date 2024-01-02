package com.seulah.seulahdms.service;

import com.seulah.seulahdms.entity.ScreenName;
import com.seulah.seulahdms.repository.EligibilityQuestionsRepository;
import com.seulah.seulahdms.repository.ScreenRepository;
import com.seulah.seulahdms.request.MessageResponse;
import com.seulah.seulahdms.request.ScreenRequest;
import com.seulah.seulahdms.response.ScreenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.seulah.seulahdms.utils.Constants.SUCCESS;

@Service
public class ScreenService {
    private final ScreenRepository screenRepository;
    ScreenResponse sc;
    private final EligibilityQuestionsRepository eligibilityQuestionsRepository;

    public ScreenService(ScreenRepository screenRepository,
                         EligibilityQuestionsRepository eligibilityQuestionsRepository) {
        this.screenRepository = screenRepository;
        this.eligibilityQuestionsRepository = eligibilityQuestionsRepository;
    }

    public ResponseEntity<MessageResponse> addScreen(ScreenRequest screenRequest) {
        if (screenRequest == null) {
            return new ResponseEntity<>(new MessageResponse(null, null, false), HttpStatus.BAD_REQUEST);
        }
        List<Long> questionIds = screenRequest.getQuestionIds();
        if (questionIds != null && !questionIds.isEmpty()) {
            List<ScreenName> screenNames = questionIds.stream()
                    .map(id -> {
                        ScreenName screenName = new ScreenName();
                        screenName.setScreenHeading(screenRequest.getScreenHeading());
                        screenName.setSetId(screenRequest.getSetId());
                        screenName.setQuestionIds(id);
                        return screenName;
                    }).toList();

            screenRepository.saveAll(screenNames);

            return new ResponseEntity<>(new MessageResponse(SUCCESS, screenNames, false), HttpStatus.OK);
        }

        return new ResponseEntity<>(new MessageResponse(null, null, false), HttpStatus.BAD_REQUEST);
    }


    public ScreenName getScreen() {
        return screenRepository.findByScreenHeading("home");
    }

    public ResponseEntity<?> getQuestionCheck(String questionId) {
        if (screenRepository.existsByQuestionIds(Long.parseLong(questionId))) {
            List<String> screenNameList = new ArrayList<>();
            List<ScreenName> items = screenRepository.findByQuestionIds(Long.parseLong(questionId));
            items.forEach(item -> {
                if (!screenNameList.contains(item.getScreenHeading())) {
                    screenNameList.add(item.getScreenHeading());
                }
            });
            sc = new ScreenResponse(true, screenNameList, "Question is already Exists");
            return ResponseEntity.ok().body(sc);
        } else {
            sc = new ScreenResponse(false, null, "No Record found");
            return ResponseEntity.badRequest().body(sc);
        }
    }

    public ResponseEntity<MessageResponse> getScreenBySetId(Long setId) {
        List<ScreenName> screenNames = screenRepository.findBySetId(setId);
        Map<String, List<Object>> map = new HashMap<>();

        screenNames.forEach(screenName -> {
            List<Object> questionList = new ArrayList<>();
            eligibilityQuestionsRepository.findById(screenName.getQuestionIds()).ifPresent(questionList::add);

            map.put(screenName.getScreenHeading(), questionList);
        });

        return new ResponseEntity<>(new MessageResponse(SUCCESS, map, false), HttpStatus.OK);
    }

}
