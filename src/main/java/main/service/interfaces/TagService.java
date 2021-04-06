package main.service.interfaces;

import main.model.Tag;

import java.util.List;

public interface TagService {
  List<Tag> getTagsForPost(List<String> strings);
}
