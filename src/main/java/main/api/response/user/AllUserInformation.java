package main.api.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AllUserInformation {
    private UserWithPhoto user;
    private String email;
    private boolean moderation;
    private int moderationCount;
    private boolean settings;
}