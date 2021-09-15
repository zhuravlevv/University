package org.example.models;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ResourceStore {

    private List<Resource> resources;

    public ResourceStore() {
        resources = new ArrayList<>();
    }

    @PostConstruct
    public void fillResources() throws Exception {
        List<File> files = Files.walk(Paths.get("src/main/resources/"))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());
        Map<File, List<String>> filesWithWords = new HashMap<>();
        Map<String, Integer> wordsWithNumberOfFilesContainingWord = new HashMap<>();
        List<String> allWords = files.stream()
                .flatMap(file -> {
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        StringBuilder resultStringBuilder = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            resultStringBuilder.append(line).append(" ");
                        }
                        List<String> words = Arrays.stream(resultStringBuilder.toString().split("[ \n,.;:\"'»«()—!0-9\\[\\]]"))
                                .filter(word -> !word.isEmpty())
                                .map(String::toLowerCase)
                                .collect(Collectors.toList());
                        for (String word : words) {
                            wordsWithNumberOfFilesContainingWord.computeIfPresent(word, (key, value) -> value + 1);
                            wordsWithNumberOfFilesContainingWord.putIfAbsent(word, 1);
                        }
                        filesWithWords.put(file, words);
                        return words.stream();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return Stream.empty();
                })
                .collect(Collectors.toList());
        Map<String, Integer> allWordsWithCount = new HashMap<>();
        for (String newWord : allWords) {
            if (!allWordsWithCount.containsKey(newWord)) {
                int count = 0;
                for (String word : allWords) {
                    if (word.equals(newWord))
                        count++;
                }
                allWordsWithCount.put(newWord, count);
            }
        }
        double totalNumberOfFiles = files.size();
        for (Map.Entry<File, List<String>> entry : filesWithWords.entrySet()) {
            Map<String, Integer> wordsInFileWithCount = new HashMap<>();
            for (String newWord : entry.getValue()) {
                if (!wordsInFileWithCount.containsKey(newWord)) {
                    int count = 0;
                    for (String word : entry.getValue()) {
                        if (word.equals(newWord))
                            count++;
                    }
                    wordsInFileWithCount.put(newWord, count);
                }
            }
            Map<String, Double> wordsWithWeight = new HashMap<>();
            //слово - 5 раз (Q)
            for (String word : entry.getValue()) {
                double numberOfFilesWithWord = wordsWithNumberOfFilesContainingWord.get(word);
                double B = Math.log(totalNumberOfFiles / numberOfFilesWithWord);
                double frequencyOfWordInFile = wordsInFileWithCount.get(word);
                double A = frequencyOfWordInFile * B;
                wordsWithWeight.putIfAbsent(word, Math.abs(A));
            }
            resources.add(Resource.builder()
                    .filePath(entry.getKey().getAbsolutePath())
                    .wordsWithWeight(wordsWithWeight)
                    .build());
        }
    }

    public List<String> findMostSuitable(final String request){
        String[] words = request.split("[ \n,.;:\"'»«()—!0-9\\[\\]]");
        Map<Resource, Double> resourcesWithTotalWeight = new HashMap<>();
        for (Resource resource : resources) {
            Double resourceWeight = 0D;
            Map<String, Double> wordsWithWeight = resource.getWordsWithWeight();
            for (String word : words) {
                word = word.toLowerCase();
                if(wordsWithWeight.containsKey(word)){
                    resourceWeight += wordsWithWeight.get(word);
                }
            }
            resourcesWithTotalWeight.put(resource, resourceWeight);
        }

        return resourcesWithTotalWeight.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .filter(entry -> entry.getValue().compareTo(0D) > 0)
                .map(entry -> entry.getKey().getFilePath())
                .collect(Collectors.toList());
    }
}
