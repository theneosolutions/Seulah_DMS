package com.seulah.seulahdms.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String question;

    @ManyToOne
    @JoinColumn(name = "eligibility_question_set_id")
    @JsonIgnore
    private EligibilityQuestionSet eligibilityQuestionSet;

    @ElementCollection
    private List<String> answer;

    private String userAnswer;
}