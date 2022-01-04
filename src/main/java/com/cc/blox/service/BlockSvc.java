package com.cc.blox.service;
 
import java.util.Map;

import com.cc.blox.domain.Block; 

public interface BlockSvc {
	public Block mineBlock(Block lastBlock, Map<String, Object>  data);
	
	public void setMining(boolean status);
}
