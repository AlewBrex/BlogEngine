package main.model.repository;

import main.model.GlobalSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GlobalSettingsRepository extends JpaRepository<GlobalSettings, Integer> {
  List<GlobalSettings> findAll();

  @Query(
      value = "SELECT value FROM global_settings AS gs WHERE gs.code = 'MULTIUSER_MODE'",
      nativeQuery = true)
  boolean getMultiUser();

  @Query(
      value = "SELECT value FROM global_settings AS gs WHERE gs.code = 'POST_PREMODERATION'",
      nativeQuery = true)
  boolean getPostPremod();

  @Query(
      value = "SELECT value FROM global_settings AS gs WHERE gs.code = 'STATISTICS_IS_PUBLIC'",
      nativeQuery = true)
  boolean getStatIsPub();

  @Query(
      value =
          "SELECT count(*) FROM global_settings AS gs WHERE gs.code = 'STATISTICS_IS_PUBLIC' AND gs.value = 'NO'",
      nativeQuery = true)
  boolean codeAndValue(String code, String value);

  @Modifying
  @Query(value = "TRUNCATE TABLE global_settings", nativeQuery = true)
  void dropSettings();

  @Modifying
  @Query(
      value =
          "INSERT INTO global_settings (code, name, value) values ('MULTIUSER_MODE', 'Многопользовательский режим', :multiUser)",
      nativeQuery = true)
  void setMultiUser(String multiUser);

  @Modifying
  @Query(
      value =
          "INSERT INTO global_settings (code, name, value) values ('POST_PREMODERATION', 'Премодерация постов', :postPremod)",
      nativeQuery = true)
  void setPostPremod(String postPremod);

  @Modifying
  @Query(
      value =
          "INSERT INTO global_settings (code, name, value) values ('STATISTICS_IS_PUBLIC', 'Показывать всем статистику блога', :statIsPub)",
      nativeQuery = true)
  void setStatIsPub(String statIsPub);
}
