package com.seulah.seulahdms.service;


import com.seulah.seulahdms.entity.*;
import com.seulah.seulahdms.repository.EligibilityQuestionSetRepository;
import com.seulah.seulahdms.repository.EligibilityQuestionsRepository;
import com.seulah.seulahdms.repository.EligibilityResultRepository;
import com.seulah.seulahdms.repository.FormulaRepository;
import com.seulah.seulahdms.request.FormulaRequest;
import com.seulah.seulahdms.request.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.seulah.seulahdms.utils.Constants.NO_RECORD_FOUND;
import static com.seulah.seulahdms.utils.Constants.SUCCESS;

@Service
@Slf4j
public class FormulaService {

    private final FormulaRepository formulaRepository;
    private final EligibilityQuestionSetRepository eligibilityQuestionSetRepository;

    private final EligibilityQuestionsRepository eligibilityQuestionsRepository;
    private final EligibilityResultRepository eligibilityResultRepository;

    public FormulaService(FormulaRepository formulaRepository, EligibilityQuestionSetRepository eligibilityQuestionSetRepository, EligibilityQuestionsRepository eligibilityQuestionsRepository, EligibilityResultRepository eligibilityResultRepository) {
        this.formulaRepository = formulaRepository;
        this.eligibilityQuestionSetRepository = eligibilityQuestionSetRepository;
        this.eligibilityQuestionsRepository = eligibilityQuestionsRepository;
        this.eligibilityResultRepository = eligibilityResultRepository;
    }

    public ResponseEntity<MessageResponse> createCalculation(FormulaRequest formulaRequest, Long setId) {
        Optional<EligibilityQuestionSet> eligibilityQuestionSet = eligibilityQuestionSetRepository.findById(setId);
        if (eligibilityQuestionSet.isPresent()) {
            if (eligibilityQuestionSet.get().getFormula() != null) {
                log.error("Formula already exist against the set id : {}", setId);
                return new ResponseEntity<>(new MessageResponse("Formula Already Exist ", null, false), HttpStatus.BAD_REQUEST);
            }
            Formula formula = new Formula();
            formula.setFormulaName(formulaRequest.getFormulaName());
            Set<String> uniqueWords = new HashSet<>();
            List<String> duplicateWords = new ArrayList<>();

            formulaRequest.getFormula().forEach(formulaValue -> {
                if (isMathematicalOperator(formulaValue)) {
                    return;
                }
                String normalizedFormulaValue = formulaValue.toLowerCase();
                if (!uniqueWords.add(normalizedFormulaValue)) {
                    duplicateWords.add(formulaValue);
                }
            });

            if (!duplicateWords.isEmpty()) {
                log.error("formula have duplicate name");
                return new ResponseEntity<>(new MessageResponse("Duplicate words: " + duplicateWords, null, false), HttpStatus.BAD_REQUEST);
            }
            formula.setFormula(formulaRequest.getFormula());
            formula.setOperation(formulaRequest.getOperation());
            formula.setValue(formulaRequest.getValue());
            formula.setScreenName(formulaRequest.getScreenName());
            formula.setEligibilityQuestionSet(eligibilityQuestionSet.get());
            formulaRepository.save(formula);
            log.info("Formula created successfully");
            return new ResponseEntity<>(new MessageResponse("Created", formula, false), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new MessageResponse(NO_RECORD_FOUND, null, false), HttpStatus.OK);
    }

    private boolean isMathematicalOperator(String value) {
        return value.matches("[+\\-*/%]");
    }


    public ResponseEntity<MessageResponse> getFormula() {
        List<Formula> formulaList = formulaRepository.findAll();
        if (!formulaList.isEmpty()) {
            log.info("Formula getting successfully");
            return new ResponseEntity<>(new MessageResponse(SUCCESS, formulaList, false), HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponse(NO_RECORD_FOUND, null, false), HttpStatus.OK);
    }

    public ResponseEntity<MessageResponse> deleteFormulaBySetId(Long setId) {
        Optional<EligibilityQuestionSet> optionalEligibilityQuestionSet = eligibilityQuestionSetRepository.findById(setId);
        if (optionalEligibilityQuestionSet.isPresent()) {
            EligibilityQuestionSet eligibilityQuestionSet = optionalEligibilityQuestionSet.get();
            if (eligibilityQuestionSet.getFormula() != null) {
                eligibilityQuestionSet.setFormula(null);
                eligibilityQuestionSetRepository.save(eligibilityQuestionSet);
                log.info("Formula deleted by set id {}", setId);
                return new ResponseEntity<>(new MessageResponse(SUCCESS, null, false), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(new MessageResponse(NO_RECORD_FOUND, null, false), HttpStatus.OK);
    }

    public ResponseEntity<MessageResponse> calculateFormula(Long setId, List<Map<String, Object>> userInputList, String userId) {
        EligibilityQuestionSet questionSet = eligibilityQuestionSetRepository.findById(setId).orElse(null);

        if (questionSet == null || questionSet.getFormula() == null) {
            return new ResponseEntity<>(new MessageResponse("Invalid input or formula", null, false), HttpStatus.BAD_REQUEST);
        }
        Formula formula = questionSet.getFormula();

        try {
            List<String> formulaHeadings = formula.getFormula();
            List<Map<String, Double>> filteredUserInputList = userInputList.stream()
                    .map(map -> map.entrySet().stream()
                            .filter(entry -> entry.getValue() instanceof Number)
                            .collect(Collectors.toMap(
                                    keyEntry -> getQuestionHeadingByIdAndSetId(setId, Long.valueOf(keyEntry.getKey())),
                                    valueEntry -> handleDynamicValue(valueEntry.getValue()),
                                    (existing, replacement) -> existing != null ? existing : replacement
                            )))
                    .collect(Collectors.toList());
            double result = calculateDynamicFormula(formulaHeadings, filteredUserInputList);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("Result", result);
            boolean isEligible = evaluateCondition(result, formula.getOperation(), formula.getValue());
            formula.setIsEligible(isEligible);
            formulaRepository.save(formula);
            responseData.put("Operation", formula.getOperation());
            responseData.put("Value", formula.getValue());
            responseData.put("isEligible", isEligible);
            EligibilityResult eligibilityResult = eligibilityResultRepository.findByUserId(userId);
            if (eligibilityResult == null) {
                eligibilityResult = new EligibilityResult();
            }
            eligibilityResult.setUserId(userId);
            eligibilityResult.setNumericQuestionEligibility(isEligible);

            eligibilityResultRepository.save(eligibilityResult);

            return new ResponseEntity<>(new MessageResponse(SUCCESS, responseData, false), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage(), null, false), HttpStatus.BAD_REQUEST);
        }
    }

    public String getQuestionHeadingByIdAndSetId(Long setId, Long questionId) {
        return eligibilityQuestionSetRepository.findById(setId)
                .map(eligibilityQuestionSet ->
                        eligibilityQuestionSet.getQuestions().stream()
                                .filter(questionSet -> questionSet.getId().equals(questionId))
                                .findFirst()
                                .map(questionSet -> eligibilityQuestionsRepository.findByQuestion(questionSet.getQuestion()))
                                .filter(eligibilityQuestions -> eligibilityQuestions.getHeading() != null)
                                .map(EligibilityQuestions::getHeading)
                                .orElse(null))
                .orElse(null);
    }


    private Double handleDynamicValue(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof List) {

            List<?> list = (List<?>) value;
            return list.stream()
                    .filter(item -> item instanceof Number)
                    .mapToDouble(item -> ((Number) item).doubleValue())
                    .sum();
        } else {
            return 0.0;
        }
    }

    private double calculateDynamicFormula(List<String> formula, List<Map<String, Double>> userInputList) throws IllegalArgumentException {
        log.info("Calculation formula on the base of user answer {},", userInputList);
        Stack<Double> stack = new Stack<>();
        String currentOperator = "+";
        double currentMultiplier = 1.0;

        for (String token : formula) {
            if (isOperator(token)) {
                currentOperator = token;
                if (token.equals("*")) {
                    currentMultiplier = stack.pop();
                } else {
                    currentMultiplier = 1.0;
                }
            } else {
                Optional<Map<String, Double>> userVariable = userInputList.stream()
                        .filter(input -> input.containsKey(token))
                        .findFirst();

                if (userVariable.isPresent()) {
                    double value = applyOperatorAndMultiplier(currentOperator, currentMultiplier, userVariable.get().get(token));
                    stack.push(value);
                } else {
                    log.error("Variable '" + token + "' not found in userInput");
                }
            }
        }

        double result = 0.0;
        while (!stack.isEmpty()) {
            result += stack.pop();
        }

        return result;
    }

    private double applyOperatorAndMultiplier(String operator, double multiplier, double value) {
        return switch (operator) {
            case "+", "*" -> multiplier * value;
            case "-" -> -multiplier * value;
            case "/" -> value == 0.0 ? 0.0 : multiplier / value;
            case "%" -> value == 0.0 ? 0.0 : multiplier % value;
            default -> value;
        };
    }

    private boolean evaluateCondition(double result, String operation, double value) {
        return switch (operation) {
            case ">" -> result > value;
            case "<" -> result < value;
            case ">=", "≥" -> result >= value;
            case "<=", "≤" -> result <= value;
            default -> false;
        };
    }

    private boolean isOperator(String str) {
        return str.matches("[+\\-*/%]");
    }

    public ResponseEntity<MessageResponse> userEligibility(String userId) {
        EligibilityResult eligibilityResult = eligibilityResultRepository.findByUserId(userId);
        log.info("get user eligibility by user id {}", userId);
        return new ResponseEntity<>(new MessageResponse(SUCCESS, eligibilityResult, false), HttpStatus.OK);
    }

    public ResponseEntity<MessageResponse> checkAllUserEligibility(UserVerifiedType userVerifiedType) {
        List<EligibilityResult> eligibilityResults = eligibilityResultRepository.findByUserVerifiedType(userVerifiedType);
        log.info("get all user eligibility by their user type");
        return new ResponseEntity<>(new MessageResponse(SUCCESS, eligibilityResults, false), HttpStatus.OK);
    }
}
