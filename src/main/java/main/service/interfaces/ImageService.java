package main.service.interfaces;

import lombok.SneakyThrows;
import main.exception.UpSizeAtUploadImage;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
  @SneakyThrows
  String uploadFileAndResizeImage(MultipartFile multipartFile, Boolean resize) throws UpSizeAtUploadImage;

  void deletePhoto(String path);
}
