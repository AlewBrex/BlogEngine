package main.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
  private Long timestamp;
  private Integer active;
  private String title;
  private List<String> tags;
  private String text;
}
