package com.wgs.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wgs.demo.classes.Owner;

public interface OwnerRepo extends JpaRepository<Owner, Integer> {

	boolean existsByUserId(String userId);

	boolean existsByMobile(String mobile);

	List<Owner> findByUserId(String userId);

	List<Owner> findByUserIdAndPass(String uid, String pass);

	List<Owner> findByMobile(String mobile);
	
	List<Owner> findById(int id);

	List<Owner> findByUserIdAndMobile(String userId, String mobile);

	List<Owner> findByNameAndMobile(String name, String mobile);
}
