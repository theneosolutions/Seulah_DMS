package com.seulah.seulahdms.entity;

import javax.persistence.*;
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
    private List<String> options;
}
