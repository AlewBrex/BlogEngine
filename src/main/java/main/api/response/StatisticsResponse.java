package main.api.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class StatisticsResponse implements ResultResponse {
  private Integer postsCount;
  private Integer likesCount;
  private Integer dislikesCount;
  private Integer viewsCount;
  private Long firstPublication;
}
