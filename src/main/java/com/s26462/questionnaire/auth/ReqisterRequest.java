package com.s26462.questionnaire.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Reqister request.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReqisterRequest {

    private String username;
    private String password;
}
