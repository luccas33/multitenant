package luccas33.multitenant.repository;

import luccas33.multitenant.model.Tenant;
import luccas33.multitenant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmailAndPasswordAndTenant(String email, String password, Tenant tenant);

    User findByEmailAndTenant(String email, Tenant tenant);

}
