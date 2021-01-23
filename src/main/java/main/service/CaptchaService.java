package main.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.api.response.CaptchaResponse;
import main.model.CaptchaCode;
import main.repository.CaptchaCodeRepository;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
@Service
@RequiredArgsConstructor
public class CaptchaService {
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
    private final CaptchaCodeRepository captchaCodeRepository;
    private char[] alphabetAndDigits = "abd2ef3g45h6k7n89rstyz".toCharArray();

    public CaptchaResponse generateCaptcha() {
        LocalDateTime localDateTime = LocalDateTime.now().minusSeconds(time);
        captchaCodeRepository.deleteCaptcha(localDateTime);
        Cage cage = new GCage();
        String secretCode = generateSecretCode();
        String code = cage.getTokenGenerator().next();
        BufferedImage bufferedImage = cage.drawImage(code);
        Image imageScaledInstance = Scalr.resize(bufferedImage, width, height);
        BufferedImage newBufferImage = new BufferedImage(width, height, 1);
        newBufferImage.getGraphics().drawImage(imageScaledInstance, 0, 0, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(newBufferImage, format, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String encodedBytesToString = Base64.getEncoder().encodeToString(outputStream.toByteArray());
        String image = titlePath.concat(encodedBytesToString);
        CaptchaCode captchaCode = new CaptchaCode(LocalDateTime.now(), code, secretCode);
        captchaCodeRepository.save(captchaCode);
        return new CaptchaResponse(secretCode, image);
    }

    public boolean checkCaptcha(String code, String secretCode) {
        return code.equals(captchaCodeRepository.getCaptchaCodeBySecretCode(secretCode));
    }

    private String generateSecretCode() {
        return IntStream.range(0, 33).map(i -> (int) (Math.random() * (alphabetAndDigits.length - 1)))
                .mapToObj(a -> String.valueOf(alphabetAndDigits[a])).collect(Collectors.joining());
    }
}