package com.seulah.seulahdms.service;


import com.seulah.seulahdms.entity.EligibilityQuestionSet;
import com.seulah.seulahdms.entity.Formula;
import com.seulah.seulahdms.repository.EligibilityQuestionSetRepository;
import com.seulah.seulahdms.repository.FormulaRepository;
import com.seulah.seulahdms.request.FormulaRequest;
import com.seulah.seulahdms.request.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class FormulaService {

    private final FormulaRepository formulaRepository;
    private final EligibilityQuestionSetRepository eligibilityQuestionSetRepository;

    public FormulaService(FormulaRepository formulaRepository, EligibilityQuestionSetRepository eligibilityQuestionSetRepository) {
        this.formulaRepository = formulaRepository;
        this.eligibilityQuestionSetRepository = eligibilityQuestionSetRepository;
    }


    public ResponseEntity<MessageResponse> createCalculation(FormulaRequest formulaRequest, Long setId) {
        Optional<EligibilityQuestionSet> eligibilityQuestionSet = eligibilityQuestionSetRepository.findById(setId);
        if (eligibilityQuestionSet.isPresent()) {
            if(eligibilityQuestionSet.get().getFormula()!=null){
                return new ResponseEntity<>(new MessageResponse("Formula Already Exist ", null, false), HttpStatus.CREATED);
            }
            Formula formula = new Formula();
            formula.setFormulaName(formulaRequest.getFormulaName());
            formula.setFormula(formulaRequest.getFormula());
            formula.setOperation(formulaRequest.getOperation());
            formula.setValue(formulaRequest.getValue());
            formula.setEligibilityQuestionSet(eligibilityQuestionSet.get());
            formulaRepository.save(formula);
            return new ResponseEntity<>(new MessageResponse("Created", formula, false), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new MessageResponse("No Record Found", null, false), HttpStatus.CREATED);
    }


    public ResponseEntity<MessageResponse> getFormula() {
        List<Formula> formulaList = formulaRepository.findAll();
        if (!formulaList.isEmpty()) {
            return new ResponseEntity<>(new MessageResponse("Success", formulaList, false), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new MessageResponse("No Record Found", null, false), HttpStatus.CREATED);
    }

    public ResponseEntity<MessageResponse> deleteFormula(Long id) {
        Optional<Formula> formula = formulaRepository.findById(id);
        if (formula.isPresent()) {
            return new ResponseEntity<>(new MessageResponse("Success", null, false), HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponse("No Record Found", null, false), HttpStatus.OK);
    }

    public ResponseEntity<MessageResponse> calculateFormula(Long setId, List<Map<String, Double>> userInput) {
        EligibilityQuestionSet questionSet = eligibilityQuestionSetRepository.findById(setId).orElse(null);

        if (questionSet == null || questionSet.getFormula() == null) {
            return new ResponseEntity<>(new MessageResponse("Invalid input or formula", null, false), HttpStatus.BAD_REQUEST);
        }
        Formula formula = questionSet.getFormula();

        try {
            double result = calculateDynamicFormula(formula.getFormula(), userInput);
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("Result", result);
            boolean isEligible = evaluateCondition(result, formula.getOperation(), formula.getValue());
            formula.setIsEligible(isEligible);
            formulaRepository.save(formula);
            responseData.put("Operation", formula.getOperation());
            responseData.put("Value", formula.getValue());
            responseData.put("isEligible", isEligible);

            return new ResponseEntity<>(new MessageResponse("Success", responseData, false), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage(), null, false), HttpStatus.BAD_REQUEST);
        }
    }

    private double calculateDynamicFormula(List<String> formula, List<Map<String, Double>> userInput) throws IllegalArgumentException {
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
                Optional<Map<String, Double>> userVariable = userInput.stream()
                        .filter(input -> input.containsKey(token))
                        .findFirst();

                if (userVariable.isPresent()) {
                    double value = applyOperatorAndMultiplier(currentOperator, currentMultiplier, userVariable.get().get(token));
                    stack.push(value);
                } else {
                    log.info("Variable '" + token + "' not found in userInput");
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
            case ">=" -> result >= value;
            case "<=" -> result <= value;
            default -> false;
        };
    }
    private boolean isOperator(String str) {
        return str.matches("[+\\-*/%]");
    }

}
