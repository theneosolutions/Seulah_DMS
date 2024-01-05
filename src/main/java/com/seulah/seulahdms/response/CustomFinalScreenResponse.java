package com.seulah.seulahdms.response;

import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomFinalScreenResponse {
    private Long setId;
    private String message;
    Map<String, List<Object>> data = new HashMap<>();
}
