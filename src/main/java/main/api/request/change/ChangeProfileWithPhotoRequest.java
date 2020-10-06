package main.api.request.change;

import lombok.Data;

@Data
public class ChangeProfileWithPhotoRequest
{
    private String photo;
    private ChangeProfileRequest changeProfileRequest;

    public ChangeProfileWithPhotoRequest(String photo, ChangeProfileRequest changeProfileRequest)
    {
        this.photo = photo;
        this.changeProfileRequest = changeProfileRequest;
    }
}