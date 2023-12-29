package com.seulah.seulahdms.service;

import com.seulah.seulahdms.entity.EligibilityQuestionSet;
import com.seulah.seulahdms.entity.EligibilityQuestions;
import com.seulah.seulahdms.repository.EligibilityQuestionSetRepository;
import com.seulah.seulahdms.repository.EligibilityQuestionsRepository;
import com.seulah.seulahdms.repository.ScreenNameRepository;
import com.seulah.seulahdms.request.MessageResponse;
import com.seulah.seulahdms.request.ScreenNameRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Muhammad Mansoor
 */
@Service
@Slf4j
public class ScreenNameService {

    private final ScreenNameRepository screenNameRepository;

    private final EligibilityQuestionsRepository eligibilityQuestionsRepository;

    private final EligibilityQuestionSetRepository eligibilityQuestionSetRepository;

    public ScreenNameService(ScreenNameRepository screenNameRepository, EligibilityQuestionsRepository eligibilityQuestionsRepository, EligibilityQuestionSetRepository eligibilityQuestionSetRepository) {
        this.screenNameRepository = screenNameRepository;
        this.eligibilityQuestionsRepository = eligibilityQuestionsRepository;
        this.eligibilityQuestionSetRepository = eligibilityQuestionSetRepository;
    }


    public ResponseEntity<MessageResponse> addQuestionAgainstScreenName(ScreenNameRequest screenNameRequest) {
        Optional<EligibilityQuestionSet> eligibilityQuestionSet = eligibilityQuestionSetRepository.findById(screenNameRequest.getSetId());
        if (eligibilityQuestionSet.isPresent()) {
            List<EligibilityQuestions> eligibilityQuestions = eligibilityQuestionsRepository.findAllById(screenNameRequest.getQuestionIds());

        }
        return null;
    }
}
