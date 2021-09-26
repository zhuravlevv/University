package com.university.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Sentence {

    private String value;
    private Integer score;
}
