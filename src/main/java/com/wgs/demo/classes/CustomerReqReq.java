package com.wgs.demo.classes;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.stereotype.Component;


@Table(name = "Customer_req", uniqueConstraints = @UniqueConstraint(columnNames = { "accno", "mobile","email" }))
@Entity
@Component
public class CustomerReqReq {
	@Id
	private int accno;
	private String email;
	private String name;
	private String mobile;
	private int balance;
	private String gender;
	private String address;
	private String pass;

	public CustomerReqReq() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CustomerReqReq(int accno,String email, String name, String mobile, int balance, String gender, String address,String pass) {
		super();
		this.accno = accno;
		this.name = name;
		this.mobile = mobile;
		this.gender = gender;
		this.address = address;
		this.balance = balance;
		this.email=email;
		this.pass=pass;
	}

	
	
	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getAccno() {
		return accno;
	}

	public void setAccno(int accno) {
		this.accno = accno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Customer [accno=" + accno + ", email=" + email + ", name=" + name + ", mobile=" + mobile + ", balance="
				+ balance + ", gender=" + gender + ", address=" + address + ", pass=" + pass + "]";
	}
}

