package com.seulah.seulahdms.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
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

    private String screenName;

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private EligibilityQuestionSet eligibilityQuestionSet;

}
