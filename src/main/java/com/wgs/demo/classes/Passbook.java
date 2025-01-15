package com.wgs.demo.classes;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.stereotype.Component;
@Table(name = "passbook", uniqueConstraints = @UniqueConstraint(columnNames = { "trxId" }))
@Entity
@Component
public class Passbook {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String trxId;
	private int accNo;
	private String custName;
	private String trxMode;
	private String trxDate;
	private int amtBefTrx;
	private int trxAmt;
	private int currentBalance;
	public Passbook() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Passbook(int id,String trxId, int accNo, String custName, String trxMode, String trxDate, int amtBefTrx,
			int trxAmt, int currentBalance) {
		super();
		this.id=id;
		this.trxId = trxId;
		this.accNo = accNo;
		this.custName = custName;
		this.trxMode = trxMode;
		this.trxDate = trxDate;
		this.amtBefTrx = amtBefTrx;
		this.trxAmt = trxAmt;
		this.currentBalance = currentBalance;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTrxId() {
		return trxId;
	}
	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}
	public int getAccNo() {
		return accNo;
	}
	public void setAccNo(int accNo) {
		this.accNo = accNo;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getTrxMode() {
		return trxMode;
	}
	public void setTrxMode(String trxMode) {
		this.trxMode = trxMode;
	}
	public String getTrxDate() {
		return trxDate;
	}
	public void setTrxDate(String trxDate) {
		this.trxDate = trxDate;
	}
	public int getAmtBefTrx() {
		return amtBefTrx;
	}
	public void setAmtBefTrx(int amtBefTrx) {
		this.amtBefTrx = amtBefTrx;
	}
	public int getTrxAmt() {
		return trxAmt;
	}
	public void setTrxAmt(int trxAmt) {
		this.trxAmt = trxAmt;
	}
	public int getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(int currentBalance) {
		this.currentBalance = currentBalance;
	}
	@Override
	public String toString() {
		return "IndividualCustomer [id=" + id + ",trxId=" + trxId + ", accNo=" + accNo + ", custName=" + custName + ", trxMode="
				+ trxMode + ", trxDate=" + trxDate + ", amtBefTrx=" + amtBefTrx + ", trxAmt=" + trxAmt
				+ ", currentBalance=" + currentBalance + "]";
	}
	
	
	
	
}
