package luccas33.multitenant.repository;

import luccas33.multitenant.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Tenant findByNameLike(String name);

    Tenant findBySchemaName(String schemaName);

}
