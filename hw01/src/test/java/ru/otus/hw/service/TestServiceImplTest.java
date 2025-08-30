package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TestServiceImplTest {

    private IOService ioService;
    private QuestionDao questionDao;

    private TestServiceImpl service;

    @BeforeEach
    void setUp() {
        ioService = mock(IOService.class);
        questionDao = mock(QuestionDao.class);

        service = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    void shouldPrintQuestionsAndAnswers() {
        //given
        List<Question> questions = List.of(
                new Question("Is there life on Mars?", List.of(
                        new Answer("Yes", false),
                        new Answer("No", true)
                )),
                new Question("2+2?", List.of(
                        new Answer("3", false),
                        new Answer("4", true)
                ))
        );
        when(questionDao.findAll()).thenReturn(questions);

        // when
        service.executeTest();

        // then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(ioService, atLeastOnce()).printLine(captor.capture());

        List<String> printed = captor.getAllValues();

        assertThat(printed).contains("Is there life on Mars?");
        assertThat(printed).contains("1.Yes");
        assertThat(printed).contains("2.No");
        assertThat(printed).contains("2+2?");
        assertThat(printed).contains("1.3");
        assertThat(printed).contains("2.4");

        verify(ioService).printFormattedLine("Please answer the questions below%n");
    }
}
