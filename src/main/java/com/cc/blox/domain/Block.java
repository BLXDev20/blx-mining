package com.cc.blox.domain;
 
import java.util.Map;

public class Block  {
	
	public Map<String, Object> data;
	public String hash;
	public String lastHash;
	public long timeStamp;
	public long nonce;
	public int difficulty;
	public long height;
	
	public Block() {
        super();
    }
	public Block(Map<String, Object> data, String hash, String lastHash, long timeStamp, long nonce, int difficulty, long height ){
		this.data = data;
		this.hash = hash;
		this.lastHash = lastHash;
		this.timeStamp = timeStamp;
		this.nonce = nonce;
		this.difficulty = difficulty;
		this.height = height;
	}
	
	public long getHeight() {
		return height;
	}
	
	public String toString() {
        return "data: " + data
        		+ ",  hash: " +   hash 
				+ ", lastHash: " + lastHash
				+ ", timeStamp: " + timeStamp
				+ ", nonce: " + nonce
				+ ", difficulty: " + difficulty;
				
    }
}
