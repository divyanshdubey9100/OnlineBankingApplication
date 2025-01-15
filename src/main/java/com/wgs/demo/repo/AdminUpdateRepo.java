package com.wgs.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wgs.demo.classes.AdminUpdateReq;
public interface AdminUpdateRepo extends JpaRepository<AdminUpdateReq, Integer>{
	
	List<AdminUpdateReq> findByUserIdAndPass(String userId,String pass);
	List<AdminUpdateReq> findByMobileAndName(String mobile,String name);
	List<AdminUpdateReq> findByUserIdAndMobile(String UserId,String mobile);
	List<AdminUpdateReq> findByUserId(String userId);
	List<AdminUpdateReq> findById(int id);
	String findByPass(String pass);
	boolean existsByUserId(String userId);

	boolean existsByMobile(String mobile);
	

}
