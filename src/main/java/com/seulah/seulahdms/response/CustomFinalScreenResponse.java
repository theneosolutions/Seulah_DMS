package com.seulah.seulahdms.response;

import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomFinalScreenResponse {
    private Long setId;
    private String message;
    List<CustomScreenQuestions> data;

}
