package com.eighti.onebkk.utils.rsa;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class KeyGenerationExample {

	public static void main(String[] args) {
		try {
			// Initialize the key pair generator for RSA
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(2048); // You can change the key size (2048 bits is common)

			// Generate the key pair
			KeyPair keyPair = keyGen.generateKeyPair();

			// Get the public and private keys
			PublicKey publicKey = keyPair.getPublic();
			PrivateKey privateKey = keyPair.getPrivate();

			// Print out the keys in Base64 format
			System.out.println("Public Key: ");
			System.out.println(Base64.getEncoder().encodeToString(publicKey.getEncoded()));

			System.out.println("Private Key: ");
			System.out.println(Base64.getEncoder().encodeToString(privateKey.getEncoded()));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
