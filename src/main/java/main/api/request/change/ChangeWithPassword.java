package main.api.request.change;

import org.springframework.web.multipart.MultipartFile;

public class ChangeWithPassword extends ChangeTreeString {
    private Byte removePhoto;
    private MultipartFile photo;

    public ChangeWithPassword(String name, String email) {
        super(name, email);
    }

    public ChangeWithPassword(String name, String email, String password) {
        super(name, email, password);
    }

    public ChangeWithPassword(String name, String email, Byte removePhoto, MultipartFile photo) {
        super(name, email);
        this.removePhoto = removePhoto;
        this.photo = photo;
    }

    public ChangeWithPassword(String name, String email, String password, Byte removePhoto, MultipartFile photo) {
        super(name, email, password);
        this.removePhoto = removePhoto;
        this.photo = photo;
    }

    public Byte getRemovePhoto() {
        return removePhoto;
    }

    public void setRemovePhoto(Byte removePhoto) {
        this.removePhoto = removePhoto;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }
}