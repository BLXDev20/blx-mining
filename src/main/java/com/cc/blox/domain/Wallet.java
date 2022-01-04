package com.cc.blox.domain;

import java.util.Arrays;
import java.util.Objects;


public class Wallet {
	private double balance;
	private String publicKey;
	private byte[] pubKey;
	
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public byte[] getPubKey() {
		return pubKey;
	}
	public void setPubKey(byte[] pubKey) {
		this.pubKey = pubKey;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(pubKey);
		result = prime * result + Objects.hash(balance, publicKey);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Wallet other = (Wallet) obj;
		return Double.doubleToLongBits(balance) == Double.doubleToLongBits(other.balance)
				&& Arrays.equals(pubKey, other.pubKey) && Objects.equals(publicKey, other.publicKey);
	}
}
