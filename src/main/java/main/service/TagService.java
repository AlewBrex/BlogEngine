package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.tag.FullInformTag;
import main.api.response.tag.TagResponse;
import main.model.Tag;
import main.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public FullInformTag getAllTags() {
        List<Tag> tagList = tagRepository.findAll();
        return new FullInformTag(getListResponse(tagList));
    }

    public FullInformTag getTagsWithQuery(String query) {
        List<Tag> queryTagList = tagRepository.getTagByQuery(query);
        return new FullInformTag(getListResponse(queryTagList));
    }

    private List<TagResponse> getListResponse(List<Tag> list) {
        List<TagResponse> responseList = new ArrayList<>();
        int ttlCount = tagRepository.totalCount();
        for (Tag tag : list) {
            double weight = ((double) tagRepository.countIdTag(tag.getId()) / ttlCount);
            double weightRound = Math.round(weight * 100.0) / 100.0;
            responseList.add(new TagResponse(tag.getName(), weightRound));
        }
        return responseList;
    }
}