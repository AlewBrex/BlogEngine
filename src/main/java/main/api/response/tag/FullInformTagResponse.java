package main.api.response.tag;

import lombok.AllArgsConstructor;
import lombok.Data;
import main.api.response.ResultResponse;

import java.util.List;

@Data
@AllArgsConstructor
public class FullInformTagResponse implements ResultResponse {
  private List<TagResponse> tags;
}
