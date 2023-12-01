package com.seulah.seulahdms.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EligibilityQuestionSetRequest {
    @NotNull
    @NotBlank
    private String name;
}
