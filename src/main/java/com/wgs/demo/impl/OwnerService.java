package com.wgs.demo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wgs.demo.classes.Owner;
import com.wgs.demo.repo.OwnerRepo;

@Service
public class OwnerService {
	@Autowired
	OwnerRepo ownerRepo;

	public int getTokenId() {
		return (int) ownerRepo.count();
	}

	public boolean isUserIdExists(String userId) {
		boolean id = ownerRepo.existsByUserId(userId);
		if (id == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isIdExists(int userId) {
		boolean id = ownerRepo.existsById(userId);
		if (id == true) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isMobileExists(String mobile) {
		boolean mob = ownerRepo.existsByMobile(mobile);
		if (mob == true) {
			return true;
		} else {
			return false;
		}
	}

	public List<Owner> findByuId(String userId) {
		List<Owner> list = ownerRepo.findByUserId(userId);
		return list;
	}

	public boolean ownerAuthintication(String uid, String pass) {
		List<Owner> list = ownerRepo.findByUserIdAndPass(uid, pass);
		for (Owner own : list) {
			if (own.getUserId().equals(uid) && own.getPass().equals(pass)) {
				return true;
			} else
				return false;
		}
		return false;
	}

	public List<Owner> getDetaislById(int id) {
		List<Owner> list = ownerRepo.findById(id);
		return list;
	}

	public List<Owner> findMailAndMobile(String userId, String mobile) {
		List<Owner> list = ownerRepo.findByUserIdAndMobile(userId, mobile);
		return list;
	}

	public List<Owner> findUidAndMobile(String name, String mobile) {
		List<Owner> list = ownerRepo.findByNameAndMobile(name, mobile);
		return list;
	}

}
