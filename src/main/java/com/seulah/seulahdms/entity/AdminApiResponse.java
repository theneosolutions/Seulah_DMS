package com.seulah.seulahdms.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    private String successImage;
    private String successDescription;
    private String errorMessage;
    private String errorImage;
    private String errorDescription;

    private Boolean isEligible;

    private Long setId;

    private String languageCode;
}
