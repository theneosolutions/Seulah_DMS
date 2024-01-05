package com.seulah.seulahdms.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomFinalScreenResponse {
    private List<CustomScreenResponse> items;
}
