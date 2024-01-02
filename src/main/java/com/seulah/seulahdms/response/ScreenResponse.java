package com.seulah.seulahdms.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ScreenResponse {
    boolean questionExists;
    String screenHeading;
    String message;
}
