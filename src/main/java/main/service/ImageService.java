package main.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
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
public class ImageService {
    @Value("${upload_file.directory}")
    private String loadPathImage;
    @Value("${upload_file.height}")
    private int heightImage;
    @Value("${upload_file.width}")
    private int widthImage;
    @Value("${upload_file.size}")
    private int sizeImage;

    private char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private char[] digits = "1234567890".toCharArray();

    public String generateDirectoryAndName() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int q = 0; q < 3; q++) {
            for (int i = 0; i < 3; i++) {
                int a = (int) (Math.random() * (alphabet.length - 1));
                stringBuilder.append(alphabet[a]);
            }
            stringBuilder.append("/");
        }
        for (int i = 0; i < 5; i++) {
            int s = (int) (Math.random() * (digits.length - 1));
            stringBuilder.append(digits[s]);
        }
        stringBuilder.append(".jpg");
        String result = "/" + loadPathImage + stringBuilder.toString();
        return result;
    }

    @SneakyThrows
    public String resizeImage(MultipartFile multipartFile, String path) {
        Path pathImage = Paths.get(path);
        String pathToString = pathImage.toString();
        if (Files.notExists(pathImage)) {
            new File(pathToString).mkdirs();
        }
        File file = new File(pathToString);
        BufferedImage newImage = ImageIO.read(new ByteArrayInputStream(multipartFile.getBytes()));
        BufferedImage scaledImage = Scalr.resize(newImage, widthImage, heightImage);
        ImageIO.write(scaledImage, "jpg", file);
        return file.getPath();
    }

    public boolean deletePhoto(String path) {
        File file = new File(path);
        return file.delete();
    }
}