package com.university.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class KeyWordsEssay {

    private Map<String, List<String>> data;
}
