package com.hanrideb.service;

import com.hanrideb.domain.DersAnaliz;
import com.hanrideb.domain.Kayit;
import com.hanrideb.domain.TestAnaliz;
import com.hanrideb.repository.DersAnalizRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class DersAnalizService {

    private final DersAnalizRepository dersAnalizRepository;
    private final KayitService kayitService;

    public DersAnalizService(DersAnalizRepository dersAnalizRepository, KayitService kayitService) {
        this.dersAnalizRepository = dersAnalizRepository;
        this.kayitService = kayitService;
    }

    public DersAnaliz dersAnalizVeriEkle(DersAnaliz dersAnaliz, TestAnaliz testAnaliz) {
        var toplamSoru = dersAnaliz.getCozulenSoru() + testAnaliz.getDogru() + testAnaliz.getYanlis() + testAnaliz.getBos();
        var topmamDogru = dersAnaliz.getToplamDogru() + testAnaliz.getDogru();
        var topmamYanlis = dersAnaliz.getToplamYanlis() + testAnaliz.getYanlis();
        dersAnaliz.setTamamlandi(true);
        dersAnaliz.setCozulenSoru(toplamSoru);
        dersAnaliz.setToplamYanlis(topmamYanlis);
        dersAnaliz.setToplamDogru(topmamDogru);

        return dersAnalizRepository.save(dersAnaliz);
    }
}
