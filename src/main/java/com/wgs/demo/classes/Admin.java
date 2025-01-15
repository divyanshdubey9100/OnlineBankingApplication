package com.wgs.demo.classes;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.stereotype.Component;


@Table(name = "admin", uniqueConstraints = @UniqueConstraint(columnNames = { "userId", "mobile" }))
@Entity
@Component
public class Admin {
	@Id
	@GeneratedValue
	private int id;
	private String userId;
	private String name;
	private String mobile;
	private String pass;
	private String gender;
	private String address;

	public Admin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Admin(int id, String userId, String name, String mobile, String pass, String gender, String address) {
		super();
		this.id = id;
		this.userId = userId;
		this.name = name;
		this.mobile = mobile;
		this.pass = pass;
		this.gender = gender;
		this.address = address;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
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
		return "Admin [id=" + id + ", userId=" + userId + ", name=" + name + ", mobile=" + mobile + ", pass=" + pass
				+ ", gender=" + gender + ", address=" + address + "]";
	}


}
