package main.api.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class PostRequest {
  private Long timestamp;
  private Integer active;
  private String title;
  private List<String> tags;
  private String text;
}
