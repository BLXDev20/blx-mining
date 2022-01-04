package com.cc.blox.utils;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;

public class CryptoUtils {
	private static BouncyCastleProvider bouncyCastleProvider;
	public static final BouncyCastleProvider BOUNCY_CASTLE_PROVIDER = new BouncyCastleProvider();
	static {
	    bouncyCastleProvider = BOUNCY_CASTLE_PROVIDER;
	}
	
	private static Map<String, String> digiMap = new HashMap<>();
	static {
	    digiMap.put("0", "0000");
	    digiMap.put("1", "0001");
	    digiMap.put("2", "0010");
	    digiMap.put("3", "0011");
	    digiMap.put("4", "0100");
	    digiMap.put("5", "0101");
	    digiMap.put("6", "0110");
	    digiMap.put("7", "0111");
	    digiMap.put("8", "1000");
	    digiMap.put("9", "1001");
	    digiMap.put("a", "1010");
	    digiMap.put("b", "1011");
	    digiMap.put("c", "1100");
	    digiMap.put("d", "1101");
	    digiMap.put("e", "1110");
	    digiMap.put("f", "1111");
	}
	public static String toSha256(String input){		
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");	        
			//Applies sha256 to our input, 
			byte[] hash = digest.digest(input.getBytes("UTF-8"));	        
			StringBuffer hexString = new StringBuffer(); // This will contain hash as hexidecimal
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}	
	
	public static String hexToBinary(String input) {
		char[] hex = input.toCharArray();
	    String binaryString = "";
	    for (char h : hex) {
	        binaryString = binaryString + digiMap.get(String.valueOf(h));
	    }
	    return binaryString;
	}
	
	
	public static ECPublicKey decodeECPublicKey(
             byte[] pubkey) {
		
		ECPublicKey pub = null;

		try {
			KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", "BC");
			
				ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
				org.bouncycastle.math.ec.ECPoint point = ecSpec.getCurve().decodePoint(pubkey);
				ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, ecSpec);
				pub = (ECPublicKey) keyFactory.generatePublic(pubSpec);
		} catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return pub;
    }
	
	public static boolean verifySignature(byte[] pubKey, byte[] data, byte[] signature) {
		
		boolean isVerified = true;
		
		//LOGGER.info(decodeECPublicKey(pubKey));
		try {
			Signature sign = Signature.getInstance("SHA256withECDSA", bouncyCastleProvider);
			sign.initVerify(decodeECPublicKey(pubKey));
			sign.update(data);
			isVerified = sign.verify(signature);
 
			
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			
			e.printStackTrace();
		}
		
		return isVerified;
	}
	
	 public static byte[] hexStringToByteArray(String s) {
	        int len = s.length();
	        byte[] data = new byte[len / 2];
	        for (int i = 0; i < len; i += 2) {
	            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                    + Character.digit(s.charAt(i + 1), 16));
	        }
	        return data;
	 }
	 
	 public static String byteArrayToHexString(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
            sb.append(String.format("%02X", b));
        return sb.toString();
    }
	 
	 public static ECPublicKey toPublicKey(String privateKeyStr) {
		ECPublicKey publicKeyGenerated = null;
		try {
			ECPrivateKey epvt = CryptoUtils.toPrivateKey(privateKeyStr);
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", "BC");
			ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");

			org.bouncycastle.math.ec.ECPoint Q = ecSpec.getG().multiply(epvt.getD());
			byte[] publicDerBytes = Q.getEncoded(false);

			org.bouncycastle.math.ec.ECPoint point = ecSpec.getCurve().decodePoint(publicDerBytes);
			ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, ecSpec);
			publicKeyGenerated = (ECPublicKey) keyFactory.generatePublic(pubSpec);
			
		} catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return publicKeyGenerated;
	 }
	 
	 public static ECPrivateKey toPrivateKey(String privateKeyStr) {
		 ECPrivateKey epvt = null;
		 try {
			BigInteger privateKeyInt = new BigInteger(privateKeyStr, 16);
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", "BC");
			
			ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
			
			ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(privateKeyInt, ecSpec);
			
			epvt = (ECPrivateKey)keyFactory.generatePrivate(privateKeySpec);
			
		} catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return epvt;
		
	 }
}
