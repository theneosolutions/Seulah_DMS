package com.seulah.seulahdms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "eligibility_questions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EligibilityQuestions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String heading;

    @Column(nullable = false,unique = true)
    private String question;

    @Column(nullable = false)
    private String type;

    @ElementCollection
//    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
//    @Column(name = "option", nullable = false) // Explicitly specify the column data type
    private List<String> options;
}

