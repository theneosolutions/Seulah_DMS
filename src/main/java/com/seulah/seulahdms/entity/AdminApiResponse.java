package com.seulah.seulahdms.entity;

import javax.persistence.*;

import lombok.*;

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
}