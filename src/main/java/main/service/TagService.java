package main.service;

import main.api.response.tag.FullInformTag;
import main.api.response.tag.TagResponse;
import main.model.Tag;
import main.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService
{
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository)
    {
        this.tagRepository = tagRepository;
    }

    public FullInformTag getTags(String query)
    {
        List<Tag> queryTagList = tagRepository.getTagByQuery(query);
        return new FullInformTag(getListResponse(queryTagList));
    }

    public FullInformTag getTagsWithQuery()
    {
        List<Tag> tagList = tagRepository.findAll();
        return new FullInformTag(getListResponse(tagList));
    }

    private List<TagResponse> getListResponse(List<Tag> list)
    {
        List<TagResponse> responseList = new ArrayList<>();
        int ttlCount = tagRepository.totalCount();
        for (Tag tag : list)
        {
            Double weight = (double)(tagRepository.countIdTag(tag.getId()) / ttlCount);
            responseList.add(new TagResponse(tag.getName(), weight));
        }
        return responseList;
    }
}