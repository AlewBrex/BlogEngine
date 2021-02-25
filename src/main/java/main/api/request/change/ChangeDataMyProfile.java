package main.api.request.change;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class ChangeDataMyProfile {
    private String name;
    private String email;
    private String password;
    private Byte removePhoto;
    private MultipartFile photo;

    public ChangeDataMyProfile(String name, String email, String password, Byte removePhoto) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.removePhoto = removePhoto;
    }

    public ChangeDataMyProfile(String name, String email, String password, Byte removePhoto, MultipartFile photo) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.removePhoto = removePhoto;
        this.photo = photo;
    }
}