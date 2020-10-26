package main.api.request.change;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeProfileWithPhotoRequest
{
    private String photo;
    private ChangeProfileRequest changeProfileRequest;
}