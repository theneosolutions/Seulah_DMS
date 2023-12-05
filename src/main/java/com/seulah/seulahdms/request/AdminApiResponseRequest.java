package com.seulah.seulahdms.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminApiResponseRequest {

    private String successMessage;
    private String errorMessage;
    private String successImage;
    private String successDescription;
    private String errorImage;
    private String errorDescription;

    private Long setId;
}
