package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SettingsResponse {
  @JsonProperty("MULTIUSER_MODE")
  private Boolean multiuserMode;

  @JsonProperty("POST_PREMODERATION")
  private Boolean postPremoderation;

  @JsonProperty("STATISTICS_IS_PUBLIC")
  private Boolean statisticsIsPublic;
}
