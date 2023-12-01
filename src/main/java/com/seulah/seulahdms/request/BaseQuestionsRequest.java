package com.seulah.seulahdms.request;


import com.seulah.seulahdms.entity.NumericQuestion;
import com.seulah.seulahdms.entity.OtherQuestion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class BaseQuestionsRequest {

    private List<OtherQuestion> otherQuestionList;
    private List<NumericQuestion> numericQuestionList;

}
