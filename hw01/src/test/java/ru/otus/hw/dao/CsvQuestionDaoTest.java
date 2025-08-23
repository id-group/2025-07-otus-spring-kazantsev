package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CsvQuestionDaoTest {

    private TestFileNameProvider fileNameProvider;
    private CsvQuestionDao csvQuestionDao;

    @BeforeEach
    void setUp() {
        fileNameProvider = mock(TestFileNameProvider.class);
        csvQuestionDao = new CsvQuestionDao(fileNameProvider);
    }

    @Test
    void shouldLoadResource_andThenOk() {
        //given
        when(fileNameProvider.getTestFileName()).thenReturn("questions.csv");

        //when
        var questions = csvQuestionDao.findAll();

        assertThat(questions).isNotNull();
        assertThat(questions.size()).isEqualTo(3);

    }

    @Test
    void  whenExceptionThrown_thenAssertionSucceeds() {
        //given
        when(fileNameProvider.getTestFileName()).thenReturn("questions1.csv");

        Exception exception = assertThrows(QuestionReadException.class, () -> csvQuestionDao.findAll());

        assertThat(exception).isNotNull();
    }

}
