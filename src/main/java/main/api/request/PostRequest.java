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
  private long timestamp;
  private int active;
  private String title;
  private List<String> tags;
  private String text;
}
