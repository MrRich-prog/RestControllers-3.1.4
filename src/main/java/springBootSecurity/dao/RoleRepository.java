package springBootSecurity.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import springBootSecurity.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
