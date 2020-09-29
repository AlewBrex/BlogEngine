package main.api.response;

public class StatisticsResponse
{
    private int postCount;
    private int likesCount;
    private int dislikesCount;
    private int viewsCount;
    private long firstPublication;

    public StatisticsResponse statisticsResponse(int postCount, int likesCount,
                                                 int dislikesCount, int viewsCount,
                                                 long firstPublication)
    {
        this.postCount = postCount;
        this.likesCount = likesCount;
        this.dislikesCount = dislikesCount;
        this.viewsCount = viewsCount;
        this.firstPublication = firstPublication;
        return this;
    }

    public int getPostCount()
    {
        return postCount;
    }

    public int getLikesCount()
    {
        return likesCount;
    }

    public int getDislikesCount()
    {
        return dislikesCount;
    }

    public int getViewsCount()
    {
        return viewsCount;
    }

    public long getFirstPublication()
    {
        return firstPublication;
    }
}