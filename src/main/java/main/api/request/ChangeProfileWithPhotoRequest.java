package main.api.request;

public class ChangeProfileWithPhotoRequest
{
    private ChangeProfileRequest changeProfileRequest;
    private String photo;

    public ChangeProfileWithPhotoRequest(ChangeProfileRequest changeProfileRequest1, String photo)
    {
        this.changeProfileRequest = changeProfileRequest1;
        this.photo = photo;
    }
}