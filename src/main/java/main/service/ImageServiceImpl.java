package main.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import main.service.interfaces.ImageService;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    @Value("${user.upload_file.root_folder}")
    private String rootPathImage;
    @Value("${user.upload_file.directory}")
    private String loadPathImage;
    @Value("${user.upload_file.avatars_folder}")
    private String pathForAvatars;
    @Value("${user.upload_file.format}")
    private String formatFile;
    @Value("${user.upload_file.height}")
    private int heightImage;
    @Value("${user.upload_file.width}")
    private int widthImage;
    @Value("${user.upload_file.size}")
    private int sizeImage;

    private char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private char[] digits = "1234567890".toCharArray();
    private String pathUploadImage = rootPathImage + "/" + loadPathImage + "/";
    private String pathResizeImage = pathUploadImage + pathForAvatars + "/";


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
        String result = pathUploadImage + stringBuilder.toString();
        return result;
    }

    public String generateName() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int s = (int) (Math.random() * (digits.length - 1));
            stringBuilder.append(digits[s]);
        }
        stringBuilder.append(".jpg");
        String result = stringBuilder.toString();
        return result;
    }

    @SneakyThrows
    @Override
    public String resizeImage(MultipartFile multipartFile) {
        String pathImage = generateName();
        String pathUpload = pathResizeImage;
        Path path = Paths.get(pathUpload, pathImage);
        while (Files.exists(path)) {
            pathImage = generateName();
            path = Paths.get(pathUpload, pathImage);
        }
        BufferedImage newImage = ImageIO.read(new ByteArrayInputStream(multipartFile.getBytes()));
        BufferedImage bufferedImage = Scalr.resize(newImage, widthImage, heightImage);
        ImageIO.write(bufferedImage, formatFile, path.toFile());
        return path.toString();
    }

    @SneakyThrows
    @Override
    public String uploadFile(MultipartFile multipartFile) {
        String pathImage = generateName();
        String pathDirectory = generateDirectory();
        Path path = Paths.get(pathDirectory, pathImage);
        while (Files.exists(path)) {
            pathImage = generateName();
            path = Paths.get(pathDirectory, pathImage);
        }
        multipartFile.transferTo(path);
        return path.toString();
    }

    @SneakyThrows
    @Override
    public void deletePhoto(String path) {
        if (!path.isBlank()) {
            Files.deleteIfExists(Path.of(path));
        }
    }
}