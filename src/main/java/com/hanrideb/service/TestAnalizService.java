package com.hanrideb.service;

import com.hanrideb.domain.*;
import com.hanrideb.repository.TestAnalizRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TestAnalizService {

    private final TestAnalizRepository testAnalizRepository;
    private final KayitService kayitService;

    public TestAnalizService(TestAnalizRepository testAnalizRepository, KayitService kayitService) {
        this.testAnalizRepository = testAnalizRepository;
        this.kayitService = kayitService;
    }

    public List<TestAnaliz> findAllBolumBaslik(String baslik) {
        return testAnalizRepository.findAllByDersAnaliz_AitOldBolum_BolumBaslik(baslik);
    }

    public List<TestAnaliz> findAllByDersAnalizId(Long id) {
        return testAnalizRepository.findAllByDersAnaliz_Id(id);
    }

    public List<TestAnaliz> getUserTestAnalizInDers(String bolumName) throws Exception {
        DersAnaliz dersAnaliz = kayitService.getDersAnalizInKayit(bolumName);
        List<TestAnaliz> testAnalizList = testAnalizRepository.findAllByDersAnaliz_Id(dersAnaliz.getId());
        return testAnalizList;
    }
}
