package com.seulah.seulahdms.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Muhammad Mansoor
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ScreenNameRequest {

    private String screenHeading;

    private List<Long> questionIds;

    private Long setId;

    private String userId;

}
