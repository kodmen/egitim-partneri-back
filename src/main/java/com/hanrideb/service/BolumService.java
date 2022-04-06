package com.hanrideb.service;

import com.hanrideb.domain.Bolum;
import com.hanrideb.repository.BolumRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class BolumService {

    private final BolumRepository bolumRepository;

    public BolumService(BolumRepository bolumRepository) {
        this.bolumRepository = bolumRepository;
    }

    public Bolum getBolumByBolumName(String bolumName) throws Exception {
        Optional<Bolum> bolum = bolumRepository.findByBolumBaslik(bolumName);
        if (bolum.isPresent()) {
            return bolum.get();
        }
        throw new Exception("bolum bulunamadai");
    }
}
