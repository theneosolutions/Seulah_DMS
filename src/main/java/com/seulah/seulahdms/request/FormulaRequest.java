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
public class FormulaRequest {

    @NotNull
    @NotBlank
    private String formulaName;

    private List<String> formula;
    @NotNull
    @NotBlank
    private String operation;
    @NotNull
    @NotBlank
    private Double value;

    private String screenName;
}