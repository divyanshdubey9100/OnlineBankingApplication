package com.wgs.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wgs.demo.classes.Admin;
public interface AdminRepo extends JpaRepository<Admin, Integer>{
	
	List<Admin> findByUserIdAndPass(String userId,String pass);
	List<Admin> findByMobileAndName(String mobile,String name);
	List<Admin> findByUserIdAndMobile(String UserId,String mobile);
	List<Admin> findByUserId(String userId);
	List<Admin> findById(int id);
	String findByPass(String pass);
	boolean existsByUserId(String userId);

	boolean existsByMobile(String mobile);

}
