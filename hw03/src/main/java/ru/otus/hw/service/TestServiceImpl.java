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

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;


    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            ioService.printLine(question.text());

            AtomicInteger counter = new AtomicInteger();
            question.answers().forEach(
                    answer -> ioService.printLine(
                            String.format("%d.%s", counter.incrementAndGet(), answer.text())
                    )
            );

            var isAnswerValid = question.answers()
                    .get(ioService.readIntForRangeWithPromptLocalized(1, question.answers().size(),
                            "TestService.answer.invitation",
                            "TestService.answer.error.incorrectanswer") - 1)
                    .isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
