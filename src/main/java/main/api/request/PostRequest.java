package main.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class PostRequest {
    private long timestamp;
    private int active;
    private String title;
    private Set<String> tags;
    private String text;
}