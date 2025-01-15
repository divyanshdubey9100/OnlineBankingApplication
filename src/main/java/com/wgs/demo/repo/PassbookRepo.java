package com.wgs.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wgs.demo.classes.Passbook;

public interface PassbookRepo extends JpaRepository<Passbook, Integer>{

	List<Passbook> findByAccNo(int accNo);

}
