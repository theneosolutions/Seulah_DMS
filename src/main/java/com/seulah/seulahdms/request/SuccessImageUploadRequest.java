package com.seulah.seulahdms.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SuccessImageUploadRequest {

    private String successMessage;
    private String successImage;
    private String successDescription;
    private Long setId;
    private String languageCode;
}
