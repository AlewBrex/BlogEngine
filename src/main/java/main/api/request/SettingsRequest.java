package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SettingsRequest {
  @JsonProperty("MULTIUSER_MODE")
  private boolean multiuserMode;

  @JsonProperty("POST_PREMODERATION")
  private boolean postPremoderation;

  @JsonProperty("STATISTICS_IS_PUBLIC")
  private boolean statisticsIsPublic;
}
