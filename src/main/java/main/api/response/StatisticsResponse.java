package main.api.response;

import lombok.Data;

@Data
public class StatisticsResponse
{
    private int postCount;
    private int likesCount;
    private int dislikesCount;
    private int viewsCount;
    private long firstPublication;

    public StatisticsResponse(int postCount, int likesCount,
                                int dislikesCount, int viewsCount,
                                long firstPublication)
    {
        this.postCount = postCount;
        this.likesCount = likesCount;
        this.dislikesCount = dislikesCount;
        this.viewsCount = viewsCount;
        this.firstPublication = firstPublication;
    }
}