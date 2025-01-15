package com.wgs.demo.classes;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.stereotype.Component;

@Table(name = "owner", uniqueConstraints = @UniqueConstraint(columnNames = { "userId", "mobile" }))
@Entity
@Component
public class Owner {
	@Id
	@GeneratedValue
	private int id;
	private String userId;
	private String name;
	private String mobile;
	private String gender;
	private String pass;
	private String dob;
	private String address;
	private String ques1;
	private String ans1;
	private String ques2;
	private String ans2;
	public Owner() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Owner(int id, String userId, String name, String mobile, String gender, String pass, String address,
			String dob, String ques1, String ans1, String ques2, String ans2) {
		super();
		this.id = id;
		this.userId = userId;
		this.name = name;
		this.mobile = mobile;
		this.gender = gender;
		this.pass = pass;
		this.address = address;
		this.dob = dob;
		this.ques1 = ques1;
		this.ans1 = ans1;
		this.ques2 = ques2;
		this.ans2 = ans2;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getQues1() {
		return ques1;
	}
	public void setQues1(String ques1) {
		this.ques1 = ques1;
	}
	public String getAns1() {
		return ans1;
	}
	public void setAns1(String ans1) {
		this.ans1 = ans1;
	}
	public String getQues2() {
		return ques2;
	}
	public void setQues2(String ques2) {
		this.ques2 = ques2;
	}
	public String getAns2() {
		return ans2;
	}
	public void setAns2(String ans2) {
		this.ans2 = ans2;
	}
	@Override
	public String toString() {
		return "Owner [id=" + id + ", userId=" + userId + ", name=" + name + ", mobile=" + mobile + ", gender=" + gender
				+ ", pass=" + pass + ", address=" + address + ", dob=" + dob + ", ques1=" + ques1 + ", ans1=" + ans1
				+ ", ques2=" + ques2 + ", ans2=" + ans2 + "]";
	}
	
}
