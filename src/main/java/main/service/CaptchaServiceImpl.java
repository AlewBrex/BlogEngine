package main.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.api.response.CaptchaResponse;
import main.model.CaptchaCode;
import main.model.repository.CaptchaCodeRepository;
import main.service.interfaces.CaptchaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

@Log4j2
@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {
    @Value("${captcha.image.title_path}")
    private String titlePath;
    @Value("${captcha.image.format}")
    private String format;
    @Value("${captcha.image.height}")
    private int height;
    @Value("${captcha.image.width}")
    private int width;
    @Value("${captcha.time}")
    private long time;
    @Value("${captcha.length_key}")
    private int lengthKey;

    private final CaptchaCodeRepository captchaCodeRepository;
    private char[] alphabetAndDigits = "abd2ef3g45h6k7n89rstyz".toCharArray();

    @Override
    public CaptchaResponse generateCaptcha() {
        LocalDateTime localDateTime = LocalDateTime.now().minusSeconds(time);
        captchaCodeRepository.deleteCaptcha(localDateTime);

        Cage cage = new GCage();
        String code = cage.getTokenGenerator().next();

        BufferedImage bufferedImage = cage.drawImage(code);
        Image result = bufferedImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        BufferedImage newBufferImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        newBufferImage.getGraphics().drawImage(result, 0, 0, null);

        String encodedBytesToString = Base64.getEncoder().encodeToString(code.getBytes(StandardCharsets.UTF_8));

        CaptchaCode captchaCode = new CaptchaCode(LocalDateTime.now(), code, encodedBytesToString);
        captchaCodeRepository.save(captchaCode);

        String image = titlePath + ", " + Base64.getEncoder().encodeToString(getImageToByte(newBufferImage, format));

        return new CaptchaResponse(encodedBytesToString, image);
    }

    @Override
    public Boolean checkCaptcha(String code, String secretCode) {
        CaptchaCode captchaCode = captchaCodeRepository.getCaptchaCodeBySecretCode(secretCode);
        return captchaCode != null && captchaCode.getCode().equals(code);
    }

    private byte[] getImageToByte(BufferedImage bufferedImage, String format) {
        byte[] bytes = new byte[0];

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            ImageIO.write(bufferedImage, format, outputStream);
            outputStream.flush();
            bytes = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }
}