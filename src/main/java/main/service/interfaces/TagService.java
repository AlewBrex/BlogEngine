package main.service.interfaces;

import main.api.response.ResultResponse;
import main.model.Tag;

import java.util.List;

public interface TagService {
  List<Tag> getTagsForPost(List<String> strings);

  ResultResponse getAllTags();

  ResultResponse getTagsWithQuery(String query);
}
