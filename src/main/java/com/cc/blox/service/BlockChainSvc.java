package com.cc.blox.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cc.blox.domain.Block;
 

public interface BlockChainSvc {
	
	public ArrayList<Block> getBlocks();
	
	public void replaceChain(ArrayList<Block> chain);

	public Map<String, Object> getAddressDetails();
	
	public boolean addBlock(Map<String, Object>  data);

	public void syncChains();

	public List<Block> getPaginatedBlocks(int page, int itemsPerPage);

	public void syncNode();
}
