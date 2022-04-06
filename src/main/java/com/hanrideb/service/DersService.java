package com.hanrideb.service;

import com.hanrideb.domain.Bolum;
import com.hanrideb.domain.Ders;
import com.hanrideb.domain.Mufredat;
import org.springframework.stereotype.Service;

@Service
public class DersService {

    private final BolumService bolumService;

    public DersService(BolumService bolumService) {
        this.bolumService = bolumService;
    }

    public Ders getDersByBolumName(String bolumName) throws Exception {
        Bolum bolum = bolumService.getBolumByBolumName(bolumName);
        for (Mufredat m : bolum.getMufredatlars()) {
            for (Bolum b : m.getBolumlers()) {
                if (b.getBolumBaslik().equals(bolumName)) {
                    return m.getMufredatDers();
                }
            }
        }

        throw new Exception("bolume göre ders bulunamadi");
    }
}
