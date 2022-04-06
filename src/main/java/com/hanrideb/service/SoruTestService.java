package com.hanrideb.service;

import com.hanrideb.domain.*;
import com.hanrideb.repository.SoruTestRepository;
import com.hanrideb.repository.TestAnalizRepository;
import com.hanrideb.service.dto.ResultsOfExam;
import com.hanrideb.service.dto.TestAnswerDto;
import com.hanrideb.service.exception.TestAlreadyUsedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class SoruTestService {

    private final SoruTestRepository soruTestRepository;
    private final KayitService kayitService;
    private final TestAnalizRepository testAnalizRepository;
    private final OgrenciService ogrenciService;

    public SoruTestService(
        SoruTestRepository soruTestRepository,
        KayitService kayitService,
        TestAnalizRepository testAnalizRepository,
        OgrenciService ogrenciService
    ) {
        this.soruTestRepository = soruTestRepository;
        this.kayitService = kayitService;
        this.testAnalizRepository = testAnalizRepository;
        this.ogrenciService = ogrenciService;
    }

    public Optional<List<SoruTest>> getByBolumName(String bolum) {
        return soruTestRepository.findAllByTestBolum_BolumBaslik(bolum);
    }

    public DersAnaliz kayitlardanDersAnaliziBul(List<Kayit> kayits, SoruTest test) {
        for (Kayit k : kayits) {
            for (DersAnaliz d : k.getDersAnalizleris()) {
                if (d.getAitOldBolum().getBolumBaslik().equals(test.getTestBolum().getBolumBaslik())) {
                    return d;
                }
            }
        }

        return null;
    }

    //todo bu fonkison çok iş yapıyor bunu parçalara ayırabiliriz
    public ResultsOfExam testAnaliz(TestAnswerDto dto) throws Exception {
        ResultsOfExam result = new ResultsOfExam();
        TestAnaliz testAnaliz = new TestAnaliz();

        List<Kayit> kayits = kayitService.getUserKayit();

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

        // testin sonuçlarını veri tabanına kayıt ediyoruz
        testAnaliz.setTestId(dto.getTestId());
        testAnaliz.setDogru(result.getCountOfCorrect());
        testAnaliz.setYanlis(result.getCountOfWrong());
        testAnaliz.setBos(result.getCountOfBlank());
        testAnaliz.setNet(netHesapla(result.getCountOfCorrect(), result.getCountOfWrong()));
        testAnaliz.setDersAnaliz(kayitlardanDersAnaliziBul(kayits, test));
        testAnaliz.setTamamlandi(true);
        if (!testAnalizRepository.existsByTestIdAndDersAnaliz_Id(dto.getTestId(), kayitlardanDersAnaliziBul(kayits, test).getId())) {
            testAnaliz = testAnalizRepository.save(testAnaliz);
        } else {
            throw new TestAlreadyUsedException();
        }

        ogrenciService.OgrenciPuanArttir(testAnaliz.getNet());

        return result;
    }

    public float netHesapla(int d, int y) {
        float yanlis = y;
        return d - (yanlis / 3);
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
