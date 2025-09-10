package hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.service.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {TestServiceImpl.class, LocalizedIOServiceImpl.class})
class TestServiceImplTest {

    @MockitoBean
    private LocalizedIOServiceImpl ioService;
    @MockitoBean
    private QuestionDao questionDao;

    @Autowired
    private TestServiceImpl service;

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

        when(ioService.readIntForRangeWithPromptLocalized(eq(1), eq(2), anyString(), anyString()))
            .thenReturn(2);

        // when
        service.executeTestFor(new Student("Dron", "Ka"));

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

        verify(ioService).printLineLocalized("TestService.answer.the.questions");
    }
}
