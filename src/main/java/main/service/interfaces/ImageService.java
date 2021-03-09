package main.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadFile(MultipartFile multipartFile);

    String resizeImage(MultipartFile multipartFile);

    void deletePhoto(String path);
}
