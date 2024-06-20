package mate.academy.bookstoreapp.repository.role;

import java.util.Optional;
import mate.academy.bookstoreapp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(Role.RoleName roleName);
}
