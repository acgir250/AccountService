package com.omnirio.accountservice.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "account")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long accountId;

	@Column(name = "account_type", nullable = false)
	private String accountType;

	@Column(name = "open_date")
	private String openDate;

	@Column(name = "customer_id", unique = true, nullable = false)
	private long customerId;

	@Column(name = "customer_name", nullable = false)
	private String customerName;

	@Column(name = "branch", nullable = false)
	private String branch;

	@Column(name = "minor_indicator", length = 2)
	private String minorIndicator;

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getMinorIndicator() {
		return minorIndicator;
	}

	public void setMinorIndicator(String minorIndicator) {
		this.minorIndicator = minorIndicator;
	}

}
