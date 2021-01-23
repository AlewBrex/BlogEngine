package main.repository;

import main.model.GlobalSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GlobalSettingsRepository extends JpaRepository<GlobalSettings, Integer> {
    List<GlobalSettings> findAll();

    GlobalSettings findByCode(String code);

    boolean codeAndValue(String code, String value);
}