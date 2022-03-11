package com.hanrideb.service;

import com.hanrideb.domain.Tag;
import com.hanrideb.repository.TagRepository;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag getTag(String tag) {
        return tagRepository.findByName(tag);
    }
}
