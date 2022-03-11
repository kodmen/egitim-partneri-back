package com.hanrideb.service;

import com.hanrideb.domain.Yorum;
import com.hanrideb.repository.YorumRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class YorumService {

    private final YorumRepository yorumRepository;

    YorumService(YorumRepository yorumRepository) {
        this.yorumRepository = yorumRepository;
    }

    public List<Yorum> getByFormIdAllYorum(long id) {
        return yorumRepository.findByFormYorum_Id(id);
    }
}
