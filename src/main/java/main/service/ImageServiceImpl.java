package main.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import main.exception.UpSizeAtUploadImage;
import main.service.interfaces.ImageService;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

//  @Value("${file.upload_directory}")
  private final String loadPathImage = "upload";

  @Value("${file.format}")
  private String formatFile;

  @Value("${file.height}")
  private int heightImage;

  @Value("${file.width}")
  private int widthImage;

  @Value("${file.size}")
  private int sizeImage;

  private final char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
  private final char[] digits = "1234567890".toCharArray();
  private final String pathResizeImage = loadPathImage + "/";

  public String generateDirectory() {
    StringBuilder stringBuilder = new StringBuilder();
    for (int q = 0; q < 3; q++) {
      for (int i = 0; i < 3; i++) {
        int a = (int) (Math.random() * (alphabet.length - 1));
        stringBuilder.append(alphabet[a]);
      }
      if (q != 2) {
        stringBuilder.append("/");
      }
    }
    return pathResizeImage + stringBuilder.toString();
  }

  public String generateName() {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < 5; i++) {
      int s = (int) (Math.random() * (digits.length - 1));
      stringBuilder.append(digits[s]);
    }
    stringBuilder.append(".jpg");
    return stringBuilder.toString();
  }

  @Override
  @SneakyThrows
  public String uploadFileAndResizeImage(MultipartFile multipartFile) throws UpSizeAtUploadImage {
    if (multipartFile.getSize() > sizeImage || multipartFile.isEmpty()) {
      throw new UpSizeAtUploadImage("Размер файла превышает допустимый размер");
    }
    String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
    assert extension != null;
    if (extension.equals("jpg") || extension.equals("png")) {
      String pathImage = generateName();
      String pathUpload = generateDirectory();
      String path = (pathUpload + "/" + pathImage).replaceAll("\\\\", "/");
      Path path1 = Paths.get(pathUpload, pathImage);
      boolean p = new File(pathUpload).mkdirs();
      Files.createFile(path1);
      BufferedImage newImage = ImageIO.read(new ByteArrayInputStream(multipartFile.getBytes()));
      BufferedImage bufferedImage = Scalr.resize(newImage, widthImage, heightImage);
      ImageIO.write(bufferedImage, formatFile, path1.toFile());
      return path;
    }
    return null;
  }

  @SneakyThrows
  @Override
  public void deletePhoto(String path) {
    if (!path.isBlank()) {
      Files.deleteIfExists(Path.of(path));
    }
  }
}
