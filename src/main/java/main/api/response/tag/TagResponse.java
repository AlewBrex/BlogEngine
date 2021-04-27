package main.api.response.tag;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TagResponse {
  private String name;
  private Double weight;
}
