package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.tag.FullInformTagResponse;
import main.api.response.tag.TagResponse;
import main.model.Tag;
import main.model.repository.TagRepository;
import main.service.interfaces.TagService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public FullInformTagResponse getAllTags() {
        List<Tag> tagList = tagRepository.findAll();
        List<TagResponse> responseList = getListResponse(tagList);
        return new FullInformTagResponse(responseList);
    }

    public FullInformTagResponse getTagsWithQuery(String query) {
        List<Tag> queryTagList = tagRepository.getTagByQuery(query);
        List<TagResponse> responseList = getListResponse(queryTagList);
        return new FullInformTagResponse(responseList);
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