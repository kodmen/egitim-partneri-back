package com.hanrideb.service;

import com.hanrideb.domain.SoruTest;
import com.hanrideb.repository.SoruTestRepository;
import com.hanrideb.service.dto.ResultsOfExam;
import com.hanrideb.service.dto.TestAnswerDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class SoruTestService {

    private final SoruTestRepository soruTestRepository;

    public SoruTestService(SoruTestRepository soruTestRepository) {
        this.soruTestRepository = soruTestRepository;
    }

    public Optional<List<SoruTest>> getByBolumName(String bolum) {
        return soruTestRepository.findAllByTestBolum_BolumBaslik(bolum);
    }

    public ResultsOfExam testAnaliz(TestAnswerDto dto) {
        ResultsOfExam result = new ResultsOfExam();

        SoruTest test = soruTestRepository.getById(dto.getTestId());

        List<String> cevapAnahtari = cevapAnahtariOlustur(test.getCevaplar());
        for (int i = 0; i < cevapAnahtari.size(); i++) {
            if (dto.getAnswers().get(i).equals("") || dto.getAnswers().get(i) == null) {
                result.increaseOfBlank();
            } else if (cevapAnahtari.get(i).equals(dto.getAnswers().get(i))) {
                result.increaseOfCorrect();
            } else {
                result.increaseOfWrong();
            }
        }
        return result;
    }

    public List<String> cevapAnahtariOlustur(String cevaplar) {
        List<String> cevapAnahtari = new ArrayList<>();
        String regexIfade = "[a-zA-Z]";
        Pattern pattern = Pattern.compile(regexIfade);
        Matcher matcher = pattern.matcher(cevaplar);
        while (matcher.find()) {
            cevapAnahtari.add(matcher.group());
        }
        return cevapAnahtari;
        //1-e,2-b,3-a
    }
}
