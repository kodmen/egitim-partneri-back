package com.hanrideb.service;

import com.hanrideb.domain.Entry;
import com.hanrideb.domain.Tag;
import com.hanrideb.repository.EntryRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EntryService {

    private final EntryRepository entryRepository;
    private final TagService tagService;

    public EntryService(EntryRepository entryRepository, TagService tagService) {
        this.entryRepository = entryRepository;
        this.tagService = tagService;
    }

    public List<Entry> getByTagsName(String tag) {
        Tag tagList = tagService.getTag(tag);

        List<Entry> tags = new ArrayList<Entry>();
        tagList
            .getEntries()
            .forEach(e -> {
                tags.add(entryRepository.findOneWithEagerRelationships(e.getId()).get());
            });

        return tags;
    }
}
