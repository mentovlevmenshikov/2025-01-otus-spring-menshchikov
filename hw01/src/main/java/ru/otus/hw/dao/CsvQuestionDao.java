package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        try {
            InputStream testFileInputStream = getTestFile();
            List<QuestionDto> questionDtos = parseTestFile(testFileInputStream);
            return mapDtoToDao(questionDtos);
        } catch (Exception e) {
            throw new QuestionReadException(e.getMessage(), e);
        }
    }

    private InputStream getTestFile() throws FileNotFoundException {
        String testFileName = fileNameProvider.getTestFileName();
        InputStream inputStream = getClass().getResourceAsStream(testFileName);
        if (inputStream == null) {
            String errMsg = String.format("File %s with questions not found.", testFileName);
            throw new FileNotFoundException(errMsg);
        }
        return inputStream;
    }

    private List<QuestionDto> parseTestFile(InputStream inputStream) throws IOException {
        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            CsvToBean<QuestionDto> csvToBean =  new CsvToBeanBuilder<QuestionDto>(reader)
                    .withType(QuestionDto.class)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .build();
            return csvToBean.parse();
        }
    }

    private List<Question>  mapDtoToDao(List<QuestionDto> questionDtos) {
        return questionDtos.stream()
                .map(QuestionDto::toDomainObject)
                .collect(Collectors.toList());
    }
}
