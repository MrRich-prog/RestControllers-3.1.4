package springBootRest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import springBootRest.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findById(long id);
}
