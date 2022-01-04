package com.cc.blox.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cc.blox.domain.Block;
import com.cc.blox.service.BlockChainSvc;
import com.cc.blox.service.TransactionPoolSvc;
 
@RestController
public class BlockChainController {
	
	@Autowired BlockChainSvc blockChainSvc; 
	@Autowired TransactionPoolSvc transactionPoolSvc; 
	
	@RequestMapping(value="/api/myaddress", method=RequestMethod.GET)
	public Map<String, Object> getPublicAddress() {
		return blockChainSvc.getAddressDetails();
	}
	
	@RequestMapping(value="/api/blocks", method=RequestMethod.GET)
	public ArrayList<Block> getBlocks() {
		return blockChainSvc.getBlocks();
	}
	
	@RequestMapping(value="/api/transaction/pool", method=RequestMethod.GET)
	public Map<String, Object> getTransactionPoolMap() {
		return transactionPoolSvc.getTransactionPoolMap();
	}
	
	
	@RequestMapping(value="/api/blocks/page", method=RequestMethod.GET)
	public List<Block> getPaginatedBlocks(
			@RequestParam(value="page", defaultValue = "1") int page,
			@RequestParam(value="itemsPerPage", defaultValue = "10") int itemsPerPage) {
		
		if(page <0) {
			page = 1;
		}
		page = page -1;
		return blockChainSvc.getPaginatedBlocks(page, itemsPerPage);
	}
}
