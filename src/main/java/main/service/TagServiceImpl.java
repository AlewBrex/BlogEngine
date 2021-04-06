package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.tag.FullInformTagResponse;
import main.api.response.tag.TagResponse;
import main.model.Tag;
import main.model.repository.PostRepository;
import main.model.repository.Tag2PostRepository;
import main.model.repository.TagRepository;
import main.service.interfaces.TagService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

  private final TagRepository tagRepository;
  private final Tag2PostRepository tag2PostRepository;
  private final PostRepository postRepository;

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

    int ttlCount = postRepository.countPostForModStatusAccepted().get();
    List<Object[]> objects = postRepository.nameTagAndCountPosts();
    Object[] popular = objects.get(0);

    BigInteger countPostForPopularTag = (BigInteger) popular[1];
    double sl = ttlCount / countPostForPopularTag.doubleValue();
    double coefficient = twoDot(sl);
    objects.forEach(
        s -> {
          String name = (String) s[0];
          BigInteger countPost = (BigInteger) s[1];
          double w = countPost.doubleValue() / ttlCount;
          double weight = twoDot(w);
          double r;
          if (countPostForPopularTag.intValue() != 0) {
            if (name.equals(popular[0].toString())) {
              r = 1.00;
            } else {
              double ew = weight * coefficient;
              r = twoDot(ew);
            }
            responseList.add(new TagResponse(name, r));
          }
        });
    return responseList;
  }

  private Double twoDot(Double d) {
    return Math.round(d * 100.0) / 100.0;
  }

  @Override
  public List<Tag> getTagsForPost(List<String> strings) {
    List<Tag> tagList = new ArrayList<>();
    for (String t : strings) {
      Tag tag = tagRepository.getTagByName(t);
      if (tag == null) {
        tag = new Tag(t);
        tagRepository.save(tag);
      }
      tagList.add(tag);
    }
    return tagList;
  }
}
