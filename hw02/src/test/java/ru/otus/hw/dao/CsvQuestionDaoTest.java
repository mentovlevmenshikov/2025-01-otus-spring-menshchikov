package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
class CsvQuestionDaoTest {

    @Mock
    TestFileNameProvider fileNameProvider;

    @Test
    @DisplayName("Throw QuestionReadException if file not exists")
    void fileNotFound() {
        Mockito.when(fileNameProvider.getTestFileName()).thenReturn("someFile.csv");
        assertThatExceptionOfType(QuestionReadException.class).isThrownBy(() -> {
            QuestionDao questionDao = new CsvQuestionDao(fileNameProvider);
            questionDao.findAll();
        });
    }

    @Test
    @DisplayName("3 questions if file exists")
    void fileReadSuccessfully() {
        Mockito.when(fileNameProvider.getTestFileName()).thenReturn("questionsExists.csv");
        QuestionDao questionDao = new CsvQuestionDao(fileNameProvider);
        assertThat(questionDao.findAll()).hasSize(3);
    }
}