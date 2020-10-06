package main.api.response.user;

import lombok.Data;

@Data
public class AllUserInformation
{
    private UserWithPhoto user;
    private String email;
    private boolean moderation;
    private int moderationCount;
    private boolean settings;

    public AllUserInformation(UserWithPhoto user, String email,
                              boolean moderation, int moderationCount,
                              boolean settings)
    {
        this.user = user;
        this.email = email;
        this.moderation = moderation;
        this.moderationCount = moderationCount;
        this.settings = settings;
    }
}