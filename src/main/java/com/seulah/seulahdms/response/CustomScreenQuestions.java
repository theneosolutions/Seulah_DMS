package com.seulah.seulahdms.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomScreenQuestions {
    private String screenName;
    private List<QuestionResponse> questions;

}
