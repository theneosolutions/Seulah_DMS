package com.seulah.seulahdms.entity;


import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class EligibilityResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private Boolean otherQuestionEligibility;
    private Boolean numericQuestionEligibility;

    @Enumerated(value = EnumType.STRING)
    private UserVerifiedType userVerifiedType;

}
