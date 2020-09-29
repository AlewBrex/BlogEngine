package main.api.response;

import java.util.Map;
import java.util.Set;

public class CalendarResponse
{
    private Set<Integer> years;
    private Map<String, Integer> posts;

    public CalendarResponse calendarResponse(Set<Integer> years, Map<String, Integer> posts)
    {
        this.years = years;
        this.posts = posts;
        return this;
    }

    public Set<Integer> getYears()
    {
        return years;
    }

    public Map<String, Integer> getPosts()
    {
        return posts;
    }
}