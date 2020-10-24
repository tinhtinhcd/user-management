package miu.edu.usermanagement.repository;

import miu.edu.usermanagement.entity.Role;
import miu.edu.usermanagement.entity.User;
import miu.edu.usermanagement.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {

}
