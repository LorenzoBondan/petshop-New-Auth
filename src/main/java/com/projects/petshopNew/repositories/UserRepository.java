package com.projects.petshopNew.repositories;

import com.projects.petshopNew.entities.User;
import com.projects.petshopNew.projections.UserDetailsProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByCpf(String cpf);

    @Query("SELECT DISTINCT obj FROM User obj " +
            "WHERE (UPPER(obj.name) LIKE UPPER(CONCAT('%', :name, '%')) ) ORDER BY obj.name")
    Page<User> find(String name, Pageable pageable);

    void deleteByCpf(String cpf);

    @Query(nativeQuery = true, value = """
				SELECT tb_user.cpf AS username, tb_user.password, tb_role.id AS roleId, tb_role.authority
				FROM tb_user
				INNER JOIN tb_user_role ON tb_user.cpf = tb_user_role.user_cpf
				INNER JOIN tb_role ON tb_role.id = tb_user_role.role_id
				WHERE tb_user.cpf = :cpf
			""")
    List<UserDetailsProjection> searchUserAndRolesByCpf(String cpf);
}
