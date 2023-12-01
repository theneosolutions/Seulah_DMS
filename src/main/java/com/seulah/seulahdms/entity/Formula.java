package com.seulah.seulahdms.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import jakarta.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Formula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true)
    private String formulaName;
    @ElementCollection
    private List<String> formula;
    @Column(nullable = false)
    private String operation;
    @Column(nullable = false)
    private Double value;

    private Boolean isEligible;

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    @JoinColumn(name = "eligibility_question_set_id")
    private EligibilityQuestionSet eligibilityQuestionSet;

}
