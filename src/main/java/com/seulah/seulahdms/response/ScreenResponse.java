package com.seulah.seulahdms.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ScreenResponse {
    boolean questionExists;
    List<String> screenHeading;
    String message;
}
