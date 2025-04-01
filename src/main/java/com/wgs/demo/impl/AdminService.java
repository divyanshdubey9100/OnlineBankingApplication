package com.wgs.demo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wgs.demo.classes.Admin;
import com.wgs.demo.repo.AdminRepo;
import com.wgs.demo.repo.AdminUpdateRepo;

@Service
public class AdminService {
	@Autowired
	AdminRepo adminRepo;
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
	public boolean isUpdateRequested(String userId) {
		boolean id = adminUpdateRepo.existsByUserId(userId);
		if (id == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isIdExists(int userId) {
		boolean id = adminRepo.existsById(userId);
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

	public List<Admin> findByuId(String userId) {
		List<Admin> list = adminRepo.findByUserId(userId);
		return list;
	}

	public boolean adminAuthintication(String uid, String pass) {
		List<Admin> list = adminRepo.findByUserIdAndPass(uid, pass);
		for (Admin reg : list) {
			if (reg.getUserId().equals(uid) && reg.getPass().equals(pass)) {
				return true;
			} else
				return false;
		}
		return false;
	}

	public List<Admin> findMobileAndName(String mobile, String name) {
		List<Admin> list = adminRepo.findByMobileAndName(mobile, name);
		return list;
	}

	public List<Admin> findUidAndMobile(String uid, String mobile) {
		List<Admin> list = adminRepo.findByUserIdAndMobile(uid, mobile);
		return list;
	}

}
