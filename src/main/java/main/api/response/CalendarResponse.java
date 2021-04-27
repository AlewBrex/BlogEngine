package main.api.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class CalendarResponse implements ResultResponse {
  private List<Integer> years;
  private Map<String, Integer> posts;
}
