package main.api.response;

import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public class CalendarResponse
{
    private Set<Integer> years;
    private Map<String, Integer> posts;

    public CalendarResponse(Set<Integer> years,
                            Map<String, Integer> posts)
    {
        this.years = years;
        this.posts = posts;
    }
}