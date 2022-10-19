package luccas33.multitenant.repository;

import luccas33.multitenant.model.AppVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {

    AppVersion findByVersionCode(String versionCode);

}
