package com.wgs.demo.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wgs.demo.classes.Customer;
import com.wgs.demo.repo.CustRepo;
import com.wgs.demo.repo.PassbookRepo;

@Component
public class MethodService {
	@Autowired
	CustRepo custRepo;
	@Autowired
	PassbookRepo pbookRepo;
	private static int incr=1;

	public boolean isAccExists(int accno) {
		boolean acc = custRepo.existsById(accno);
		if (acc == true) {
			return true;
		} else {
			return false;
		}
	}
	

	public int getTokenId() {
		return (int) custRepo.count();
	}

	public boolean isMobileExists(String mobile) {
		boolean mob = custRepo.existsByMobile(mobile);
		if (mob == true) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isPersonExists(String name) {
		boolean n = custRepo.existsByName(name);
		if (n == true) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isMailExists(String email) {
		boolean mail = custRepo.existsByEmail(email);
		if (mail == true) {
			return true;
		} else {
			return false;
		}
	}

	public List<Customer> findMailAndMobile(String mail, String mobile) {
		List<Customer> list = custRepo.findByEmailAndMobile(mail, mobile);
		return list;
	}

	public List<Customer> findUidAndMobile(String name, String mobile) {
		List<Customer> list = custRepo.findByNameAndMobile(name, mobile);
		return list;
	}

	public boolean customerAuthintication(String email, String pass) {
		List<Customer> list = custRepo.findByEmailAndPass(email, pass);
		for (Customer cust : list) {
			if (cust.getEmail().equals(email) && cust.getPass().equals(pass)) {
				return true;
			} else
				return false;
		}
		return false;
	}

	public List<Customer> findByEmail(String email) {
		List<Customer> list = custRepo.findByEmail(email);
		return list;
	}

	public int getTrxToken() {
		return (int) pbookRepo.count();
	}

	public String trxIdGen(int accno) {
		String time = new SimpleDateFormat("yyyyMMddhhmmss").format(Calendar.getInstance().getTime());
		String id=time+accno+incr++;
		return id;
	}

}