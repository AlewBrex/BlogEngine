package main.model.repository;

import main.model.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CaptchaCodeRepository extends JpaRepository<CaptchaCode, Integer> {

  @Query(value = "SELECT * FROM captcha_codes AS c_c WHERE c_c.secret_code = ?", nativeQuery = true)
  CaptchaCode getCaptchaCodeBySecretCode(String secretCode);

  @Modifying
  @Transactional
  @Query(
      value = "DELETE FROM captcha_codes AS c_c WHERE c_c.time < (NOW() - 60 MINUTE)",
      nativeQuery = true)
  void deleteCaptcha();
}
