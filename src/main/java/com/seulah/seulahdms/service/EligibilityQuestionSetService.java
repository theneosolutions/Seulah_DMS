package com.seulah.seulahdms.service;


import com.seulah.seulahdms.entity.*;
import com.seulah.seulahdms.repository.*;
import com.seulah.seulahdms.request.MessageResponse;
import com.seulah.seulahdms.request.QuestionSetResponse;
import com.seulah.seulahdms.request.QuestionValuePair;
import com.seulah.seulahdms.request.QuestionWithUserAnswerResponse;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class EligibilityQuestionSetService {
    private final EligibilityQuestionSetRepository eligibilityQuestionSetRepository;
    private final EligibilityQuestionsRepository eligibilityQuestionsRepository;

    private final QuestionSetRepository questionSetRepository;
    private final AdminApiResponseRepository adminApiResponseRepository;

    private final EligibilityResultRepository eligibilityResultRepository;

    private final MongoTemplate mongoTemplate;

    public EligibilityQuestionSetService(EligibilityQuestionSetRepository eligibilityQuestionSetRepository, EligibilityQuestionsRepository eligibilityQuestionsRepository, QuestionSetRepository questionSetRepository, AdminApiResponseRepository adminApiResponseRepository, EligibilityResultRepository eligibilityResultRepository, MongoTemplate mongoTemplate) {
        this.eligibilityQuestionSetRepository = eligibilityQuestionSetRepository;
        this.eligibilityQuestionsRepository = eligibilityQuestionsRepository;
        this.questionSetRepository = questionSetRepository;
        this.adminApiResponseRepository = adminApiResponseRepository;
        this.eligibilityResultRepository = eligibilityResultRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Transactional
    public ResponseEntity<MessageResponse> saveQuestionSet(String setName, List<Long> questionIds) {
        EligibilityQuestionSet eligibilityQuestionSet = new EligibilityQuestionSet();
        eligibilityQuestionSet.setName(setName);

        List<EligibilityQuestions> eligibilityQuestions = eligibilityQuestionsRepository.findAllById(questionIds);
        EligibilityQuestionSet finalEligibilityQuestionSet = eligibilityQuestionSet;

        eligibilityQuestions.forEach(eligibilityQuestion -> {
            QuestionSet questionSet = new QuestionSet();
            questionSet.setQuestion(eligibilityQuestion.getQuestion());
            questionSet.setEligibilityQuestionSet(finalEligibilityQuestionSet);

            finalEligibilityQuestionSet.getQuestions().add(questionSet);
        });

        eligibilityQuestionSet = eligibilityQuestionSetRepository.save(eligibilityQuestionSet);
        questionSetRepository.saveAll(eligibilityQuestionSet.getQuestions());

        return new ResponseEntity<>(new MessageResponse("Set Created Successfully", eligibilityQuestionSet, false), HttpStatus.CREATED);
    }

    public ResponseEntity<MessageResponse> getQuestionById(Long id) {
        Optional<EligibilityQuestionSet> eligibilityQuestionSet = eligibilityQuestionSetRepository.findById(id);

        if (eligibilityQuestionSet.isPresent()) {
            List<Map<String, Object>> questionsWithOptions = new ArrayList<>();

            eligibilityQuestionSet.get().getQuestions().forEach(question -> {
                EligibilityQuestions eligibilityQuestion = eligibilityQuestionsRepository.findByQuestion(question.getQuestion());
                if (eligibilityQuestion != null) {
                    Map<String, Object> questionWithOptions = new HashMap<>();
                    questionWithOptions.put("id", question.getId());
                    questionWithOptions.put("answer", question.getAnswer());
                    questionWithOptions.put("question", question.getQuestion());
                    questionWithOptions.put("options", eligibilityQuestion.getOptions());
                    questionWithOptions.put("Heading", eligibilityQuestion.getHeading());

                    questionsWithOptions.add(questionWithOptions);
                }
            });

            return new ResponseEntity<>(new MessageResponse("Success", questionsWithOptions, false), HttpStatus.OK);
        }

        return new ResponseEntity<>(new MessageResponse("No Record Found", null, false), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<MessageResponse> getQuestions() {
        List<EligibilityQuestionSet> eligibilityQuestionSets = eligibilityQuestionSetRepository.findAll();
        return new ResponseEntity<>(new MessageResponse("Success", eligibilityQuestionSets, false), HttpStatus.OK);
    }

    public ResponseEntity<MessageResponse> deleteQuestion(Long id) {
        Optional<EligibilityQuestionSet> eligibilityQuestionSet = eligibilityQuestionSetRepository.findById(id);
        if (eligibilityQuestionSet.isPresent()) {
            eligibilityQuestionSetRepository.delete(eligibilityQuestionSet.get());
            return new ResponseEntity<>(new MessageResponse("Success", null, false), HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponse("No Record Found", null, false), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<MessageResponse> updateAnswer(String setName, Long setId, Long questionId, List<String> answer) {
        Optional<EligibilityQuestionSet> optionalEligibilityQuestionSet = eligibilityQuestionSetRepository.findById(setId);

        if (optionalEligibilityQuestionSet.isEmpty()) {
            return new ResponseEntity<>(new MessageResponse("No Record Found", null, false), HttpStatus.OK);
        }
        EligibilityQuestionSet eligibilityQuestionSet = optionalEligibilityQuestionSet.get();
        Optional<QuestionSet> optionalQuestionSet = questionSetRepository.findByIdWithEligibilityQuestions(questionId);

        if (optionalQuestionSet.isPresent()) {
            QuestionSet questionSet = optionalQuestionSet.get();
            if (setName != null && !setName.isEmpty()) {
                eligibilityQuestionSet.setName(setName);
            }
            questionSet.setAnswer(answer);

            eligibilityQuestionSetRepository.save(eligibilityQuestionSet);
            questionSetRepository.save(questionSet);

            return new ResponseEntity<>(new MessageResponse("Answer Updated Successfully", eligibilityQuestionSet, false), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("QuestionSet not found with id: " + questionId, null, false), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<MessageResponse> getQuestionByIdAndSetId(Long questionId, Long setId) {
        Optional<EligibilityQuestionSet> eligibilityQuestionSet = eligibilityQuestionSetRepository.findById(setId);
        AtomicReference<Map<String, Object>> responseData = new AtomicReference<>(new HashMap<>());

        eligibilityQuestionSet.ifPresent(questionSet -> questionSet.getQuestions().forEach(question -> {
            if (question.getId().equals(questionId) && question.getQuestion() != null && !question.getQuestion().isEmpty()) {
                responseData.get().put("id", question.getId());
                responseData.get().put("question", question.getQuestion());
                EligibilityQuestions eligibilityQuestions = eligibilityQuestionsRepository.findByQuestion(question.getQuestion());
                if (eligibilityQuestions != null) {
                    responseData.get().put("Option", eligibilityQuestions.getOptions());
                }
            }
        }));

        if (!responseData.get().isEmpty()) {
            return new ResponseEntity<>(new MessageResponse("Success", responseData.get(), false), HttpStatus.OK);
        }

        return new ResponseEntity<>(new MessageResponse("No Record Found", null, false), HttpStatus.OK);
    }


    public ResponseEntity<MessageResponse> getFormulaByEligibilityQuestionSetId(Long eligibilityQuestionSetId) {
        Optional<EligibilityQuestionSet> eligibilityQuestionSetOptional = eligibilityQuestionSetRepository.findById(eligibilityQuestionSetId);

        if (eligibilityQuestionSetOptional.isPresent()) {
            EligibilityQuestionSet eligibilityQuestionSet = eligibilityQuestionSetOptional.get();
            if (eligibilityQuestionSet.getFormula() != null) {
                return new ResponseEntity<>(new MessageResponse("Success", eligibilityQuestionSet.getFormula(), false), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new MessageResponse("No Record Found", null, false), HttpStatus.OK);
    }

    public ResponseEntity<MessageResponse> getQuestionSetByNumericAndString(Long id, Boolean forUser) {
        Optional<EligibilityQuestionSet> eligibilityQuestionSet = eligibilityQuestionSetRepository.findById(id);
        Map<String, Object> responseData = new HashMap<>();
        List<Object> numericQuestions = new ArrayList<>();
        List<Object> textQuestions = new ArrayList<>();
        Set<QuestionValuePair> otherQuestions = new HashSet<>();

        eligibilityQuestionSet.ifPresent(questionSet -> {
            responseData.put("Formula", eligibilityQuestionSet.get().getFormula());
            Set<Long> processedNumericQuestionIds = new HashSet<>();
            Set<Long> processedTextQuestionIds = new HashSet<>();
            Set<Long> processedOtherQuestionIds = new HashSet<>();

            questionSet.getQuestions().forEach(question -> {
                String questionText = question.getQuestion();
                List<String> userAnswer = question.getUserAnswer();
                EligibilityQuestions eligibilityQuestions = eligibilityQuestionsRepository.findByQuestion(questionText);

                if (eligibilityQuestions != null) {
                    for (String option : eligibilityQuestions.getOptions()) {
                        String optionType = option.toLowerCase();

                        if (optionType.equals("numeric")) {
                            if (processedNumericQuestionIds.add(question.getId())) {
                                numericQuestions.add(new QuestionWithUserAnswerResponse(question.getId(), eligibilityQuestions.getHeading(), eligibilityQuestions.getQuestion(), eligibilityQuestions.getType(), eligibilityQuestions.getOptions(), userAnswer));

                            }
                        } else if (optionType.equals("text")) {
                            if (processedTextQuestionIds.add(question.getId())) {
                                textQuestions.add(new QuestionWithUserAnswerResponse(question.getId(), eligibilityQuestions.getHeading(), eligibilityQuestions.getQuestion(), eligibilityQuestions.getType(), eligibilityQuestions.getOptions(), userAnswer));

                            }
                        } else {
                            if (processedOtherQuestionIds.add(question.getId())) {
                                QuestionValuePair otherData;
                                if (forUser) {
                                    otherData = new QuestionValuePair(new EligibilityQuestions(question.getId(), eligibilityQuestions.getHeading(), eligibilityQuestions.getQuestion(), eligibilityQuestions.getType(), eligibilityQuestions.getOptions()), null, userAnswer);
                                } else {
                                    otherData = new QuestionValuePair(new EligibilityQuestions(question.getId(), eligibilityQuestions.getHeading(), eligibilityQuestions.getQuestion(), eligibilityQuestions.getType(), eligibilityQuestions.getOptions()), question.getAnswer(), userAnswer);
                                }
                                otherQuestions.add(otherData);
                            }
                        }
                    }
                }
            });
        });


        responseData.put("Numeric_Question", numericQuestions);
        responseData.put("Text_Question", textQuestions);
        responseData.put("Other_Question", otherQuestions);

        return new ResponseEntity<>(new MessageResponse("Success", responseData, false), HttpStatus.OK);
    }

    public ResponseEntity<MessageResponse> getAllDecision() {
        Map<String, QuestionSetResponse> questionSetResponsesMap = new HashMap<>();

        List<EligibilityQuestionSet> allQuestionSets = eligibilityQuestionSetRepository.findAll();

        allQuestionSets.forEach(questionSet -> {
            Long setId = questionSet.getId();
            String setName = questionSet.getName();
            Object formula = questionSet.getFormula() != null ? questionSet.getFormula() : null;

            QuestionSetResponse questionSetResponse = questionSetResponsesMap.computeIfAbsent(setId.toString(), key -> new QuestionSetResponse(setId, formula, setName));

            Set<String> processedNumericQuestions = new HashSet<>();
            Set<String> processedTextQuestions = new HashSet<>();
            Set<String> processedOtherQuestions = new HashSet<>();
            Set<String> processedQuestionKeys = new HashSet<>();

            questionSet.getQuestions().forEach(question -> {
                String questionKey = question.getQuestion() + "_" + setId;
                if (!processedQuestionKeys.contains(questionKey)) {
                    processedQuestionKeys.add(questionKey);

                    EligibilityQuestions eligibilityQuestions = eligibilityQuestionsRepository.findByQuestion(question.getQuestion());
                    if (eligibilityQuestions != null) {
                        for (String option : eligibilityQuestions.getOptions()) {
                            String optionType = option.toLowerCase();

                            QuestionValuePair questionValuePair = new QuestionValuePair(new EligibilityQuestions(question.getId(), eligibilityQuestions.getHeading(), eligibilityQuestions.getQuestion(), eligibilityQuestions.getType(), eligibilityQuestions.getOptions()), !optionType.equals("numeric") && !optionType.equals("text") ? question.getAnswer() : null, question.getUserAnswer());

                            switch (optionType) {
                                case "numeric":
                                    if (!processedNumericQuestions.contains(questionKey)) {
                                        processedNumericQuestions.add(questionKey);
                                        questionSetResponse.getNumericQuestions().add(questionValuePair);
                                    }
                                    break;
                                case "text":
                                    if (!processedTextQuestions.contains(questionKey)) {
                                        processedTextQuestions.add(questionKey);
                                        questionSetResponse.getTextQuestions().add(questionValuePair);
                                    }
                                    break;
                                default:
                                    if (!processedOtherQuestions.contains(questionKey)) {
                                        processedOtherQuestions.add(questionKey);
                                        questionSetResponse.getOtherQuestions().add(questionValuePair);
                                    }
                                    break;
                            }
                        }
                    }
                }
            });
            questionSetResponsesMap.put(setId.toString(), questionSetResponse);
        });

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("QuestionSets", new ArrayList<>(questionSetResponsesMap.values()));

        return new ResponseEntity<>(new MessageResponse("Success", responseData, false), HttpStatus.OK);
    }


    public ResponseEntity<MessageResponse> updateUserAnswer(Long id, List<HashMap<String, List<String>>> userAnswersList) {
        Optional<EligibilityQuestionSet> optionalEligibilityQuestionSet = eligibilityQuestionSetRepository.findById(id);

        if (optionalEligibilityQuestionSet.isEmpty()) {
            return new ResponseEntity<>(new MessageResponse("No Record Found", null, false), HttpStatus.OK);
        }

        EligibilityQuestionSet eligibilityQuestionSet = optionalEligibilityQuestionSet.get();

        for (HashMap<String, List<String>> userAnswers : userAnswersList) {
            for (Map.Entry<String, List<String>> entry : userAnswers.entrySet()) {
                String questionId = entry.getKey();
                List<String> answers = entry.getValue();

                Optional<QuestionSet> optionalQuestionSet = questionSetRepository.findByIdWithEligibilityQuestions(Long.valueOf(questionId));

                if (optionalQuestionSet.isPresent()) {
                    QuestionSet questionSet = optionalQuestionSet.get();
                    questionSet.setUserAnswer(answers);

                    eligibilityQuestionSetRepository.save(eligibilityQuestionSet);
                    questionSetRepository.save(questionSet);
                } else {
                    return new ResponseEntity<>(new MessageResponse("QuestionSet not found with id: " + questionId, null, false), HttpStatus.NOT_FOUND);
                }
            }
        }

        return new ResponseEntity<>(new MessageResponse("Answers Updated Successfully", eligibilityQuestionSet, false), HttpStatus.OK);
    }


    public ResponseEntity<AdminApiResponse> checkEligibility(Long setId, String userId) {
        Optional<EligibilityQuestionSet> optionalEligibilityQuestionSet = eligibilityQuestionSetRepository.findById(setId);
        if (optionalEligibilityQuestionSet.isPresent()) {
            EligibilityQuestionSet eligibilityQuestionSet = optionalEligibilityQuestionSet.get();

            boolean answersMatch = eligibilityQuestionSet.getQuestions().stream()
                    .allMatch(questionSet -> {
                        String questionText = questionSet.getQuestion();
                        EligibilityQuestions eligibilityQuestions = eligibilityQuestionsRepository.findByQuestion(questionText);

                        if (eligibilityQuestions != null && eligibilityQuestions.getOptions() != null && !eligibilityQuestions.getOptions().isEmpty()) {
                            if (!"numeric".equals(eligibilityQuestions.getOptions().get(0)) && !"text".equals(eligibilityQuestions.getOptions().get(0))) {
                                List<String> normalizedAnswer = questionSet.getAnswer().stream()
                                        .map(String::toLowerCase)
                                        .collect(Collectors.toList());

                                String normalizedUserAnswer = questionSet.getUserAnswer().get(0).toLowerCase();

                                return normalizedAnswer.contains(normalizedUserAnswer);
                            }
                        }
                        return true;
                    });

            Optional<AdminApiResponse> adminApiResponse = adminApiResponseRepository.findBySetId(setId);
            ResponseEntity<AdminApiResponse> adminApiResponseResponseEntity;
            if (answersMatch) {

                adminApiResponseResponseEntity = adminApiResponse.map(apiResponse -> new ResponseEntity<>(new AdminApiResponse(apiResponse.getId(), apiResponse.getSuccessMessage(), apiResponse.getSuccessImage(), apiResponse.getSuccessDescription(), null, null, null, true, setId), HttpStatus.OK))
                        .orElseGet(() -> new ResponseEntity<>(new AdminApiResponse(0L, null, null, null, null, null, null, true, setId), HttpStatus.OK));
                EligibilityResult eligibilityResult = eligibilityResultRepository.findByUserId(userId);
                if (eligibilityResult != null) {
                    eligibilityResult.setOtherQuestionEligibility(Boolean.TRUE);
                    if (eligibilityResult.getNumericQuestionEligibility().equals(Boolean.FALSE)) {
                        eligibilityResult.setUserVerifiedType(UserVerifiedType.DUMP);
                        mongoTemplate.save(eligibilityResult);

                    }else {
                        eligibilityResult.setUserVerifiedType(UserVerifiedType.VERIFIED);
                    }
                    eligibilityResultRepository.save(eligibilityResult);

                }

            } else {
                adminApiResponseResponseEntity = adminApiResponse.map(apiResponse -> new ResponseEntity<>(new AdminApiResponse(apiResponse.getId(), null, null, null, apiResponse.getErrorMessage(), apiResponse.getErrorImage(), apiResponse.getErrorDescription(), false, setId), HttpStatus.OK))
                        .orElseGet(() -> new ResponseEntity<>(new AdminApiResponse(0L, null, null, null, null, null, null, false, setId), HttpStatus.OK));
                EligibilityResult eligibilityResult = eligibilityResultRepository.findByUserId(userId);
                if (eligibilityResult != null) {
                    eligibilityResult.setOtherQuestionEligibility(Boolean.FALSE);
                    eligibilityResult.setUserVerifiedType(UserVerifiedType.DUMP);
                    eligibilityResultRepository.save(eligibilityResult);
                    mongoTemplate.save(eligibilityResult);
                }
            }
            return adminApiResponseResponseEntity;
        }

        return new ResponseEntity<>(new AdminApiResponse(0L, null, null, null, null, null, null, false, setId), HttpStatus.OK);
    }


}

