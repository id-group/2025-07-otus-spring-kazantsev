package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            ioService.printLine(question.text());

            AtomicInteger counter = new AtomicInteger();
            question.answers().forEach(
                    answer -> ioService.printLine(String.format("%d.%s", counter.incrementAndGet(), answer.text()))
            );

            final int min = 1;
            final int max = question.answers().size();
            var isAnswerValid = question.answers().get(ioService.readIntForRangeWithPrompt(min, max,
                    "Выберите ответ", String.format("Ответ должен быть от %d до %d", min,  max)) - 1)
                    .isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
