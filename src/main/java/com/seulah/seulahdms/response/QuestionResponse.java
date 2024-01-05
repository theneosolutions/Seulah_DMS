package com.seulah.seulahdms.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuestionResponse {
    private String heading;
    private String question;
    private String type;
    private List<String> options;
    private String languageCode;
    private String field;
    private List<String> userAnswer;
}
