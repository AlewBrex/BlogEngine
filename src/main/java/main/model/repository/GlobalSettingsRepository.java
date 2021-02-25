package main.model.repository;

import main.model.GlobalSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GlobalSettingsRepository extends JpaRepository<GlobalSettings, Integer> {
    List<GlobalSettings> findAll();

    GlobalSettings findByCode(String code);

    @Query(value = "SELECT count(*) FROM global_settings AS gs WHERE gs.code = 'STATISTICS_IS_PUBLIC' AND gs.value = 'NO'", nativeQuery = true)
    Integer codeAndValue(String code, String value);
}