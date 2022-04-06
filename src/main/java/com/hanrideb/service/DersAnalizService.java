package com.hanrideb.service;

import com.hanrideb.domain.DersAnaliz;
import com.hanrideb.domain.Kayit;
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

    public DersAnaliz getByBolumBaslik(String baslik) throws Exception {
        //ait old bolume göre değil de
        //kayitlara göre bulmam lazım
        List<Kayit> kayits = kayitService.getUserKayit();
        // usera ait kayitları getirdim

        Optional<DersAnaliz> dersAnaliz = dersAnalizRepository.findByAitOldBolum_BolumBaslik(baslik);
        if (dersAnaliz.isPresent()) {
            return dersAnaliz.get();
        }
        throw new Exception("ders analiz bulunamadi");
    }
}
