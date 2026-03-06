package com.jeferson.jecommerce.repositories;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jeferson.jecommerce.entities.User;
import com.jeferson.jecommerce.projections.UserDetailsProjection;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	//User findByEmail(String email);
	
	@Query(nativeQuery = true, value = """
			SELECT tb_user.email AS username, tb_user.password, tb_role.id AS roleId, tb_role.authority
			FROM tb_user
			INNER JOIN tb_user_role ON tb_user.id = tb_user_role.user_id
			INNER JOIN tb_role ON tb_role.id = tb_user_role.role_id
			WHERE tb_user.email = :email
		""")
	List<UserDetailsProjection> searchUserAndRolesByEmail(@Param("email")String email);
	// para o Postman teve que usar Param string email e o impor param acima para a homolgação ser realizada
	Optional<User> findByEmail(String email);
}
