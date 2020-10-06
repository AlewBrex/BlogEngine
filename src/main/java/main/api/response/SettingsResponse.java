package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SettingsResponse
{
    @JsonProperty("MULTIUSER_MODE")
    private boolean multiuserMode;

    @JsonProperty("POST_PREMODERATION")
    private boolean postPremoderation;

    @JsonProperty("STATISTICS_IS_PUBLIC")
    private boolean statisticsIsPublic;

    public SettingsResponse(boolean multiuserMode, boolean postPremoderation,
                            boolean statisticsIsPublic)
    {
        this.multiuserMode = multiuserMode;
        this.postPremoderation = postPremoderation;
        this.statisticsIsPublic = statisticsIsPublic;
    }
}