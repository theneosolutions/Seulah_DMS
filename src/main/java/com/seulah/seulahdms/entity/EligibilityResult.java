package com.seulah.seulahdms.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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
