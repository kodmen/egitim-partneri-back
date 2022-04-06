package com.hanrideb.service;

import com.hanrideb.domain.*;
import com.hanrideb.repository.DersAnalizRepository;
import com.hanrideb.repository.DersRepository;
import com.hanrideb.repository.KayitRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class KayitService {

    private final DersRepository dersRepository;
    private final KayitRepository kayitRepository;
    private final UserService userService;
    private final OgrenciService ogrenciService;
    private final DersAnalizRepository dersAnalizRepository;
    private final DersService dersService;

    public KayitService(
        DersRepository dersRepository,
        KayitRepository kayitRepository,
        UserService userService,
        OgrenciService ogrenciService,
        DersAnalizRepository dersAnalizRepository,
        DersService dersService
    ) {
        this.dersRepository = dersRepository;
        this.kayitRepository = kayitRepository;
        this.userService = userService;
        this.ogrenciService = ogrenciService;
        this.dersAnalizRepository = dersAnalizRepository;
        this.dersService = dersService;
    }

    public Kayit save(long dersId) throws Exception {
        // burda bu öğrenci bu derse kayit yaptıysa tekrar kayıt yaptıramasın

        //        if (checkTheStudentRegisteredForTheCourse(dersId)){
        //            throw new KayitBulunamadiException();
        //        }else{
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

    public Boolean checkTheStudentRegisteredForTheCourse(long dersId) throws Exception {
        Ogrenci ogrenci = ogrenciService.getByUserId();
        return kayitRepository.existsByAitOldDers_IdAndKayitOgrenci_Id(dersId, ogrenci.getId());
    }

    public List<Kayit> getUserKayit() throws Exception {
        Ogrenci ogrenci = ogrenciService.getByUserId();
        return kayitRepository.findAllByKayitOgrenci_Id(ogrenci.getId());
    }

    public Ders getDersInKayitlar(List<Kayit> kayits, String dersIsmi) throws Exception {
        for (Kayit k : kayits) {
            if (k.getAitOldDers().getIsim().equals(dersIsmi)) {
                return k.getAitOldDers();
            }
        }

        throw new Exception("ders bulunamadi");
    }

    public Kayit getKayitInKayitlar(String dersname) throws Exception {
        List<Kayit> kayits = getUserKayit();
        for (Kayit k : kayits) {
            if (k.getAitOldDers().getIsim().equals(dersname)) {
                return k;
            }
        }
        throw new Exception("kayit bulunamadi derse ait");
    }

    public Bolum getBolumInDers(Ders ders, String bolumName) throws Exception {
        for (Bolum b : ders.getDersMufredat().getBolumlers()) {
            if (b.getBolumBaslik().equals(bolumName)) {
                return b;
            }
        }
        throw new Exception("bolum bulunamadi");
    }

    public DersAnaliz getDersAnalizInBolumName(Kayit kayit, String bolumName) throws Exception {
        for (DersAnaliz d : kayit.getDersAnalizleris()) {
            if (d.getAitOldBolum().getBolumBaslik().equals(bolumName)) {
                return d;
            }
        }
        throw new Exception("Ders analiz bulunamadi");
    }

    public DersAnaliz getDersAnalizInKayit(String bolumName) throws Exception {
        Ders ders = dersService.getDersByBolumName(bolumName);
        Kayit kayit = getKayitInKayitlar(ders.getIsim());
        DersAnaliz dersAnaliz = getDersAnalizInBolumName(kayit, bolumName);
        return dersAnaliz;
    }

    public boolean dersKayitliMi(String bolumName) {
        try {
            Ders ders = dersService.getDersByBolumName(bolumName);
            Kayit kayit = getKayitInKayitlar(ders.getIsim());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
