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
            var indexAnswer = new AtomicInteger(1);
            ioService.printFormattedLine("%s", question.text());
            question.answers().forEach(answer -> ioService.printFormattedLine("%s. %s",
                    indexAnswer.getAndIncrement(), answer.text()));

            var indexAnswerChosen = ioService.readIntForRange(1, question.answers().size(),
                    "The answer number is incorrect.");

           var isAnswerValid = question.answers().get(indexAnswerChosen - 1).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
