package com.seulah.seulahdms.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EligibilityQuestionsRequest {
    @NotNull
    @NotBlank
    private String heading;
    @NotNull
    @NotBlank
    private String question;
    @NotNull
    @NotBlank
    private String type;

    private List<String> options;

    private String screenName;

}
