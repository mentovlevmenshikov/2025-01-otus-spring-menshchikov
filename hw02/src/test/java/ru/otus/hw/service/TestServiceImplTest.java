package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    @Mock
    private IOService ioService;
    @Mock
    private QuestionDao questionDao;

    @Test
    @DisplayName("Verify one call method findAll in question dao")
    void verifyOneCallFindAll() {
        TestService testService = new TestServiceImpl(ioService, questionDao);
        Student student = Mockito.mock(Student.class);
        testService.executeTestFor(student);
        Mockito.verify(questionDao, Mockito.times(1))
                .findAll();
    }

    @Test
    @DisplayName("Check save student in result")
    void checkSaveStudentInResult() {
        TestService testService = new TestServiceImpl(ioService, questionDao);
        Student student = new Student("Ivan", "Ivanov");
        TestResult result = testService.executeTestFor(student);
        assertThat(result.getStudent()).isEqualTo(student);
    }
}