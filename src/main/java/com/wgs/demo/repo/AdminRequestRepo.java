package com.wgs.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wgs.demo.classes.RequestAdmin;
public interface AdminRequestRepo extends JpaRepository<RequestAdmin, Integer>{
	
	List<RequestAdmin> findByUserIdAndPass(String userId,String pass);
	List<RequestAdmin> findByMobileAndName(String mobile,String name);
	List<RequestAdmin> findByUserIdAndMobile(String UserId,String mobile);
	List<RequestAdmin> findByUserId(String userId);
	List<RequestAdmin> findById(int id);
	String findByPass(String pass);
	boolean existsByUserId(String userId);

	boolean existsByMobile(String mobile);
	
	void deleteByMobile(String mobile);

}
