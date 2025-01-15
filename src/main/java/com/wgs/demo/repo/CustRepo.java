package com.wgs.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wgs.demo.classes.Customer;

public interface CustRepo extends JpaRepository<Customer, Integer> {

	List<Customer> findByAccno(int accno);

	List<Customer> findByName(String name);
	
	List<Customer> findByEmailAndPass(String email,String pass);
	
	List<Customer> findByEmailAndMobile(String email,String mob);
	
	List<Customer> findByNameAndMobile(String name, String mobile);

	List<Customer> findByMobile(String mobile);
	
	List<Customer> findByEmail(String email);

	boolean existsByMobile(String mobile);

	boolean existsByName(String name);
	
	boolean existsByEmail(String email);
	
	
}
