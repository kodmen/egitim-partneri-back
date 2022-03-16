package com.hanrideb.service;

import com.hanrideb.domain.*;
import com.hanrideb.repository.DersAnalizRepository;
import com.hanrideb.repository.DersRepository;
import com.hanrideb.repository.KayitRepository;
import org.springframework.stereotype.Service;

@Service
public class KayitService {

    private final DersRepository dersRepository;
    private final KayitRepository kayitRepository;
    private final UserService userService;
    private final OgrenciService ogrenciService;
    private final DersAnalizRepository dersAnalizRepository;

    public KayitService(
        DersRepository dersRepository,
        KayitRepository kayitRepository,
        UserService userService,
        OgrenciService ogrenciService,
        DersAnalizRepository dersAnalizRepository
    ) {
        this.dersRepository = dersRepository;
        this.kayitRepository = kayitRepository;
        this.userService = userService;
        this.ogrenciService = ogrenciService;
        this.dersAnalizRepository = dersAnalizRepository;
    }

    public Kayit save(long dersId) throws Exception {
        // burda bu öğrenci bu derse kayit yaptıysa tekrar kayıt yaptıramasın

        Ogrenci ogrenci = ogrenciService.getByUserId();
        Ders ders = dersRepository.getById(dersId);
        Kayit kayit = Kayit.bosKayitUret();

        kayit.setAitOldDers(ders);
        kayit.setKayitOgrenci(ogrenci);

        for (Bolum bolum : ders.getDersMufredat().getBolumlers()) {
            DersAnaliz dersAnaliz = dersAnalizRepository.save(DersAnaliz.uretDersAnalizBolum(bolum));
            kayit.getDersAnalizleris().add(dersAnaliz);
        }

        kayit = kayitRepository.save(kayit);

        return kayit;
    }
}
