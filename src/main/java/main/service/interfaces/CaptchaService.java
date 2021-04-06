package main.service.interfaces;

import main.api.response.CaptchaResponse;

public interface CaptchaService {
  CaptchaResponse generateCaptcha();

  Boolean checkCaptcha(String code, String secretCode);
}
