package com.seulah.seulahdms.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuestionWithUserAnswerResponse {
    private Long id;


    private String heading;


    private String question;

    private String type;


    private List<String> options;

    private List<String> userAnswer;

    private String screenName;
}
