package com.seulah.seulahdms.service;

import com.seulah.seulahdms.entity.NumericQuestion;
import com.seulah.seulahdms.entity.OtherQuestion;
import com.seulah.seulahdms.repository.NumericQuestionRepository;
import com.seulah.seulahdms.repository.OtherQuestionRepository;
import com.seulah.seulahdms.request.BaseQuestionsRequest;
import com.seulah.seulahdms.request.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BaseQuestionService {
    private final NumericQuestionRepository numericQuestionRepository;

    private final OtherQuestionRepository otherQuestionRepository;

    public BaseQuestionService(NumericQuestionRepository numericQuestionRepository, OtherQuestionRepository otherQuestionRepository) {
        this.numericQuestionRepository = numericQuestionRepository;
        this.otherQuestionRepository = otherQuestionRepository;
    }

    public ResponseEntity<MessageResponse> saveBaseQuestion(BaseQuestionsRequest baseQuestionsRequest) {
        List<OtherQuestion> otherQuestions = new ArrayList<>();
        baseQuestionsRequest.getOtherQuestionList().forEach(other -> {
            OtherQuestion otherQuestion = new OtherQuestion();
            otherQuestion.setQuestion(other.getQuestion());
            otherQuestion.setAnswer(other.getAnswer());
            otherQuestions.add(otherQuestion);
        });

        List<NumericQuestion> numericQuestions = new ArrayList<>();
        baseQuestionsRequest.getNumericQuestionList().forEach(numeric -> {
            NumericQuestion numericQuestion = new NumericQuestion();
            numericQuestion.setQuestion(numeric.getQuestion());
            numericQuestion.setAnswer(numeric.getAnswer());
            numericQuestions.add(numericQuestion);
        });

        List<OtherQuestion> savedOtherQuestions = otherQuestionRepository.saveAll(otherQuestions);
        List<NumericQuestion> savedNumericQuestions = numericQuestionRepository.saveAll(numericQuestions);

        Map<String, Object> map = new HashMap<>();
        map.put("numericQuestions", savedNumericQuestions);
        map.put("otherQuestions", savedOtherQuestions);

        return new ResponseEntity<>(new MessageResponse("Success", map, false), HttpStatus.CREATED);
    }

}
