package com.seulah.seulahdms.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ErrorImageUploadRequest {
    private Long setId;
    private String languageCode;
    private String errorMessage;
    private String errorImage;
    private String errorDescription;
}
