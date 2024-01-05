package com.seulah.seulahdms.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomScreenResponse {
    private String message;
    private Long setId;
    private List<CustomScreenQuestions> data;
    private Boolean error;
}
