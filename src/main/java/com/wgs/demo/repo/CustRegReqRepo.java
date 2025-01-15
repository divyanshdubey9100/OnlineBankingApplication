package com.wgs.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wgs.demo.classes.CustomerRegReq;

public interface CustRegReqRepo extends JpaRepository<CustomerRegReq, Integer> {

	boolean existsByMobile(String mobile);

	boolean existsByName(String name);
	
	boolean existsByEmail(String email);

	void deleteByAccno(int accno);
	
	
}
