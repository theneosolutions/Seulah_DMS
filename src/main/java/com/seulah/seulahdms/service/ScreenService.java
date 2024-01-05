package com.seulah.seulahdms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.seulah.seulahdms.entity.EligibilityQuestionSet;
import com.seulah.seulahdms.entity.EligibilityQuestions;
import com.seulah.seulahdms.entity.QuestionSet;
import com.seulah.seulahdms.entity.ScreenName;
import com.seulah.seulahdms.repository.EligibilityQuestionSetRepository;
import com.seulah.seulahdms.repository.EligibilityQuestionsRepository;
import com.seulah.seulahdms.repository.QuestionSetRepository;
import com.seulah.seulahdms.repository.ScreenRepository;
import com.seulah.seulahdms.request.MessageResponse;
import com.seulah.seulahdms.request.ScreenRequest;
import com.seulah.seulahdms.response.CustomScreenQuestions;
import com.seulah.seulahdms.response.CustomScreenResponse;
import com.seulah.seulahdms.response.QuestionResponse;
import com.seulah.seulahdms.response.ScreenResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.seulah.seulahdms.utils.Constants.NO_RECORD_FOUND;
import static com.seulah.seulahdms.utils.Constants.SUCCESS;

@Service
public class ScreenService {
    private final ScreenRepository screenRepository;
    ScreenResponse sc;
    private final EligibilityQuestionSetService eligibilityQuestionSetService;
    private final EligibilityQuestionsRepository eligibilityQuestionsRepository;
    private final EligibilityQuestionSetRepository eligibilityQuestionSetRepository;
    private final QuestionSetRepository questionSetRepository;
    CustomScreenQuestions customScreenQuestions = new CustomScreenQuestions();
    QuestionResponse questions = new QuestionResponse();
    List<QuestionResponse> questionList;

    public ScreenService(ScreenRepository screenRepository, EligibilityQuestionSetService eligibilityQuestionSetService, EligibilityQuestionsRepository eligibilityQuestionsRepository, EligibilityQuestionSetRepository eligibilityQuestionSetRepository,
                         QuestionSetRepository questionSetRepository) {
        this.screenRepository = screenRepository;
        this.eligibilityQuestionSetService = eligibilityQuestionSetService;
        this.eligibilityQuestionsRepository = eligibilityQuestionsRepository;
        this.eligibilityQuestionSetRepository = eligibilityQuestionSetRepository;
        this.questionSetRepository = questionSetRepository;
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
            String screenHeading = screenName.getScreenHeading();
            List<Object> questionList = map.getOrDefault(screenHeading, new ArrayList<>());

            ResponseEntity<MessageResponse> questionResponse = eligibilityQuestionSetService.getQuestionByIdAndSetId(screenName.getQuestionIds(), setId);

            if (questionResponse.getStatusCode() == HttpStatus.OK) {
                questionList.add(questionResponse.getBody());
            }

            map.put(screenHeading, questionList);
        });

        return new ResponseEntity<>(new MessageResponse(SUCCESS, map, false), HttpStatus.OK);
    }

    public List<CustomScreenResponse> getScreenWithQuestionDetailBySetId(Long setId) {
        Optional<EligibilityQuestionSet> eligibilityQuestionSet = eligibilityQuestionSetRepository.findById(setId);
        List<QuestionResponse> questionResponseList = new ArrayList<>();
        if (eligibilityQuestionSet.isPresent()) {
            List<ScreenName> screenNames = screenRepository.findBySetId(setId);
            Map<String, String> map = new HashMap<>();
            List<CustomScreenResponse> items = new ArrayList<>();
            List<CustomScreenQuestions> customScreenQuestionsList = new ArrayList<>();
            screenNames.forEach(screenName -> {
                String screenHeading = screenName.getScreenHeading();
                //   questionList = map.getOrDefault(screenHeading, new ArrayList<>());

                ResponseEntity<MessageResponse> questionResponse = eligibilityQuestionSetService.getQuestionByIdAndSetId(screenName.getQuestionIds(), setId);

                if (questionResponse.getStatusCode() == HttpStatus.OK && questionResponse.hasBody()) {
                    String jsonInString = new Gson().toJson(questionResponse.getBody().getData());

                    JSONObject mJSONObject = new JSONObject(jsonInString);
                    String question = mJSONObject.getString("question");

                    EligibilityQuestions eligibilityQuestion = eligibilityQuestionsRepository.findByQuestion(question);
                    Optional<QuestionSet> optionalQuestionSet = questionSetRepository.findByIdWithEligibilityQuestions(screenName.getQuestionIds());
                    Map<String, Object> combinedObject = new HashMap<>();
                    combinedObject.put("screenName", screenHeading);
                    combinedObject.put("setId", setId);

                    customScreenQuestions.setScreenName(screenHeading);
                    //  questionList.add(combinedObject);

                    questions.setHeading(eligibilityQuestion.getHeading());
                    questions.setQuestion(eligibilityQuestion.getQuestion());
                    questions.setType(eligibilityQuestion.getType());
                    questions.setOptions(eligibilityQuestion.getOptions());
                    questions.setLanguageCode(eligibilityQuestion.getLanguageCode());
                    questions.setField(eligibilityQuestion.getField());
                    questions.setUserAnswer(optionalQuestionSet.get().getUserAnswer());
                    questionResponseList.add(questions);
                    customScreenQuestions.setQuestions(questionResponseList);
                }

                customScreenQuestionsList.add(customScreenQuestions);

            });
            items.add(new CustomScreenResponse(SUCCESS, setId, customScreenQuestionsList, false));


            return items;
        }
        return null;
        //return new ResponseEntity<>(new MessageResponse(NO_RECORD_FOUND, null, false), HttpStatus.BAD_REQUEST);
    }

    //    public ResponseEntity<MessageResponse> getScreenWithQuestionDetailBySetId(Long setId) {
//        Optional<EligibilityQuestionSet> eligibilityQuestionSet = eligibilityQuestionSetRepository.findById(setId);
//
//        if (eligibilityQuestionSet.isPresent()) {
//            List<ScreenName> screenNames = screenRepository.findBySetId(setId);
//            Map<String, List<Object>> map = new HashMap<>();
//
//            screenNames.forEach(screenName -> {
//                String screenHeading = screenName.getScreenHeading();
//                questionList = map.getOrDefault(screenHeading, new ArrayList<>());
//
//                ResponseEntity<MessageResponse> questionResponse = eligibilityQuestionSetService.getQuestionByIdAndSetId(screenName.getQuestionIds(), setId);
//
//                if (questionResponse.getStatusCode() == HttpStatus.OK && questionResponse.hasBody()) {
//                    String jsonInString = new Gson().toJson(questionResponse.getBody().getData());
//
//                    JSONObject mJSONObject = new JSONObject(jsonInString);
//                    String question = mJSONObject.getString("question");
//
//                    EligibilityQuestions eligibilityQuestion = eligibilityQuestionsRepository.findByQuestion(question);
//                    Optional<QuestionSet> optionalQuestionSet = questionSetRepository.findByIdWithEligibilityQuestions(screenName.getQuestionIds());
//                    Map<String, Object> combinedObject = new HashMap<>();
//                    combinedObject.put("screenName",screenHeading);
//                    combinedObject.put("setId", setId);
//                    combinedObject.put("id", eligibilityQuestion.getId());
//                    combinedObject.put("heading", eligibilityQuestion.getHeading());
//                    combinedObject.put("question", eligibilityQuestion.getQuestion());
//                    combinedObject.put("type", eligibilityQuestion.getType());
//                    combinedObject.put("options", eligibilityQuestion.getOptions());
//                    combinedObject.put("languageCode", eligibilityQuestion.getLanguageCode());
//                    combinedObject.put("field", eligibilityQuestion.getField());
//                    combinedObject.put("userAnswer", optionalQuestionSet.isPresent() ? optionalQuestionSet.get().getUserAnswer() : "");
//
//                    questionList.add(combinedObject);
//                }
//
//                map.put(screenHeading.replaceAll("\\s",""), questionList);
//            });
//
//            return new ResponseEntity<>(new CustomScreenResponse(SUCCESS,String.valueOf(setId), questionList, false), HttpStatus.OK);
//        }
//
//        return new ResponseEntity<>(new MessageResponse(NO_RECORD_FOUND, null, false), HttpStatus.BAD_REQUEST);
//    }
    public List<CustomScreenResponse> getAllScreenWithQuestionDetail() {
        List<EligibilityQuestionSet> eligibilityQuestionSet = eligibilityQuestionSetRepository.findAll();
        List<CustomScreenResponse> responses = new ArrayList<>();

        eligibilityQuestionSet.forEach(set -> {
            List<ScreenName> screenNameList = screenRepository.findBySetId(set.getId());
            if (!screenNameList.isEmpty()) {
                responses.addAll(getScreenWithQuestionDetailBySetId(set.getId()));
            }
        });
        return responses;
    }


}
