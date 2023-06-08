package com.s26462.questionnaire.questionnaire.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Insurer dto.
 */
@Getter
@Setter
public class InsurerDto {
    private String firstName;
    private String lastName;
    private String pesel;

    @Override
    public String toString() {
        return  "Imię ubezpieczonego = " + firstName + '\n' +
                "Nazwisko ubezpieczonego = " + lastName + '\n' +
                "PESEL ubezpieczonego = " + pesel + '\n';
    }

}
