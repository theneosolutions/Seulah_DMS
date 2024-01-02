package com.seulah.seulahdms.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminApiResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String successMessage;
    @Lob
    private String successImage;
    private String successDescription;
    private String errorMessage;
    @Lob
    private String errorImage;
    private String errorDescription;

    private Boolean isEligible;

    private Long setId;

    private String languageCode;
}
