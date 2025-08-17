package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        List<Question> questions = questionDao.findAll();

        questions.forEach(q -> {
            ioService.printLine(q.text());

            AtomicInteger counter = new AtomicInteger();
            q.answers().forEach(
                    answer -> ioService.printLine(String.format("%d.%s", counter.incrementAndGet(), answer.text()))
            );
        });
    }
}
