package com.seulah.seulahdms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EligibilityQuestionSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "eligibilityQuestionSet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionSet> questions = new ArrayList<>();

    @OneToOne(mappedBy = "eligibilityQuestionSet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Formula formula;

}
