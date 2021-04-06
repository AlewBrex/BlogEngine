package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsResponse implements ResultResponse {
  private int postsCount;
  private int likesCount;
  private int dislikesCount;
  private int viewsCount;
  private long firstPublication;
}
