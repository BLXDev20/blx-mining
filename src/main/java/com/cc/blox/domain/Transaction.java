package com.cc.blox.domain;

import java.util.Map;
import java.util.Objects;
 

public class Transaction {
	private String hash;
	private Wallet senderWallet;
	private String recipient;
	private double amount;
	private Map<String, Object> outputMap;
	private Map<String, Object> input;
	
	
	public Wallet getSenderWallet() {
		return senderWallet;
	}
	public void setSenderWallet(Wallet senderWallet) {
		this.senderWallet = senderWallet;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Map<String, Object> getOutputMap() {
		return outputMap;
	}
	public void setOutputMap(Map<String, Object> outputMap) {
		this.outputMap = outputMap;
	}
	public Map<String, Object> getInput() {
		return input;
	}
	public void setInput(Map<String, Object> input) {
		this.input = input;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(amount, hash, input, outputMap, recipient, senderWallet);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		return Double.doubleToLongBits(amount) == Double.doubleToLongBits(other.amount)
				&& Objects.equals(hash, other.hash) && Objects.equals(input, other.input)
				&& Objects.equals(outputMap, other.outputMap) && Objects.equals(recipient, other.recipient)
				&& Objects.equals(senderWallet, other.senderWallet);
	}
	
}
