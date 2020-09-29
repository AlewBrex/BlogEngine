package main.api.response.user;

public class AllUserInformation
{
    private UserResponse userResponse;
    private String email;
    private boolean moderation;
    private int moderationCount;
    private boolean settings;

    public AllUserInformation allUserInformation(UserResponse userResponse1, String email,
                              boolean moderation, int moderationCount,
                              boolean settings)
    {
        this.userResponse = userResponse1.userTreeResponse(
                userResponse1.getId(),
                userResponse1.getName(),
                userResponse1.getPhoto());
        this.email = email;
        this.moderation = moderation;
        this.moderationCount = moderationCount;
        this.settings = settings;
        return this;
    }



    public String getEmail()
    {
        return email;
    }

    public boolean isModeration()
    {
        return moderation;
    }

    public int getModerationCount()
    {
        return moderationCount;
    }

    public boolean isSettings()
    {
        return settings;
    }
}