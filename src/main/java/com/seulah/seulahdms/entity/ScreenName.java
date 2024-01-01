package com.seulah.seulahdms.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * @author Muhammad Mansoor
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ScreenName {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String screenHeading;

    private Long questionIds;

    private Long setId;

    private String userId;

}
