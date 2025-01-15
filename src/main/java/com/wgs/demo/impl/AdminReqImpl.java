package com.wgs.demo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wgs.demo.classes.RequestAdmin;
import com.wgs.demo.repo.AdminRequestRepo;
import com.wgs.demo.repo.AdminUpdateRepo;

@Component
public class AdminReqImpl {
	@Autowired
	AdminRequestRepo adminRepo;
	@Autowired
	RequestAdmin regReq;
	@Autowired
	AdminUpdateRepo adminUpdateRepo;

	public int getTokenId() {
		return (int) adminRepo.count();
	}

	public boolean isUserIdExists(String userId) {
		boolean id = adminRepo.existsByUserId(userId);
		if (id == true) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isMobileExists(String mobile) {
		boolean mob = adminRepo.existsByMobile(mobile);
		if (mob == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public List<RequestAdmin> findAllReq(){
		return adminRepo.findAll();
	}

	public Object findUpdateReq() {
		return adminUpdateRepo.findAll();
	}
}