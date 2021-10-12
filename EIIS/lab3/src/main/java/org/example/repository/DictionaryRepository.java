package org.example.repository;

import org.example.models.Dictionary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DictionaryRepository extends CrudRepository<Dictionary, Integer> {

    Optional<Dictionary>  findByRussianWord(String russianWord);
}
