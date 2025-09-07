package hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CsvQuestionDao.class)
class CsvQuestionDaoTest {

    @Autowired
    private CsvQuestionDao csvQuestionDao;
    @MockitoBean
    private TestFileNameProvider fileNameProvider;

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
