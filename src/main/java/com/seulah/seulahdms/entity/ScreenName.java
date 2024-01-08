package com.seulah.seulahdms.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScreenName that = (ScreenName) o;
        return Objects.equals(screenHeading, that.screenHeading);
    }

    @Override
    public int hashCode() {
        return Objects.hash(screenHeading);
    }
}
