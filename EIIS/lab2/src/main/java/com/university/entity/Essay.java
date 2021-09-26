package com.university.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Essay {

    private ClassicEssay classicEssay;
    private KeyWordsEssay keyWordsEssay;

}
