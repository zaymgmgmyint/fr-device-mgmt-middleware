package com.eighti.onebkk.utils.rsa;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class RSATest {

	// Convert Base64 string to PublicKey
	public static PublicKey getPublicKeyFromBase64(String base64Key) throws Exception {
		byte[] decoded = Base64.getDecoder().decode(base64Key);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(keySpec);
	}

	// Convert Base64 string to PrivateKey
	public static PrivateKey getPrivateKeyFromBase64(String base64Key) throws Exception {
		byte[] decoded = Base64.getDecoder().decode(base64Key);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(keySpec);
	}

	// Encrypt data using RSA with OAEP and SHA-1
	public static String encrypt(String plaintext, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
		return Base64.getEncoder().encodeToString(encryptedBytes); // Return base64-encoded ciphertext
	}

	// Decrypt data using RSA with OAEP and SHA-1
	public static String decrypt(String ciphertext, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
		return new String(decryptedBytes);
	}

	public static void main(String[] args) {
		try {
			// Example Base64-encoded public and private keys (replace with your actual
			// keys)
			String base64PublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnsOmY/n+1YbaomeG1Q3/Pgpn4AAtW0BN8abCL+JHa5XS1NUT7TTrS7rkfFAZASkVejaOyHiVfl+ulsOorB8wcYIQ4BUNiGjYTLlmZ/+44d3A7g+NloJPGHPQvzr4rBcjoLLi4+gpRp+hge9Nw5Lh8i3eL4dyljqSyvOAwy/jFJLhDY8Voj+CViZCEpdI5s+drBhn3QNynqidgcfGwp450J4IU981mz4YxlAzUNa1dVaWolRrvCkOLMP+Tlqy1CBJp4WzvRJAhCQ94v73LgzaT1YFMcm1ll7bahqCu2pKDyyJKYOUoJoJ8V8blqtkgxckcjukxdCJVbX+pT9mT9CrOwIDAQAB"; // Replace with your base64-encoded public key
			String base64PrivateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCew6Zj+f7VhtqiZ4bVDf8+CmfgAC1bQE3xpsIv4kdrldLU1RPtNOtLuuR8UBkBKRV6No7IeJV+X66Ww6isHzBxghDgFQ2IaNhMuWZn/7jh3cDuD42Wgk8Yc9C/OvisFyOgsuLj6ClGn6GB703DkuHyLd4vh3KWOpLK84DDL+MUkuENjxWiP4JWJkISl0jmz52sGGfdA3KeqJ2Bx8bCnjnQnghT3zWbPhjGUDNQ1rV1VpaiVGu8KQ4sw/5OWrLUIEmnhbO9EkCEJD3i/vcuDNpPVgUxybWWXttqGoK7akoPLIkpg5SgmgnxXxuWq2SDFyRyO6TF0IlVtf6lP2ZP0Ks7AgMBAAECggEAAQGLB3DC5+mbbzEEiCnS9KR9rCUzckoJnMAyyUom2EVwyz5U7sEEBq48bkhF02uDYN7wznbgGmpbRyTQNcjrKHhz5EUt4bVhPYBD3Pd+7i5Ae3q+JFqZr2dk/kyyxtPdwI83MdhihuurNMjxenHCXylAa2qjdVpJL8wrsNDw+YleP/710DDuFg5gaIg8dwOOEBw7l3RvmlLgFQwvRLRgx2WC71qF1Tch4Ojj1iHoOm0pO9GPXh82+PZe094G2xRRfkmh9IHPDgUemavvllOHGtjZYm4G7KhNrTi189iRiVz+0y+vU9eRb2dXL8Abu3BR7IgKmuedZ7Qje0iscQVZdQKBgQDeai9l0IrkwGoXM/V5DJ6N/BiKR4NrlC02w0B/Ksn7PeuMNZBHuCghrpL0w8M+LSUpBuABYHdT28+cMkPZ3HI12Wz+ppOE/XxgaOY0AMXkYnJwcMaaA307V6LO79WV3gQygNsHwLlpAtd7C7OHgvyxYGqUdKPPh4E3PYAjejSy1wKBgQC2vPKISV/NvkvO6hTO2phIRaQJraiY271CJ0D76y/kEUB3KavwJwYROcJ+QllfCPEBJtvgk8Drgmu94YJfutpBh/VQIdSYVUyp+LNpYC8Rnz6zCuSaXisqTUYUBRk7bHpG69TtEF+GwAhNR5toJ5BiAgLpbwRpf2vdFRMDO9qiPQKBgDYhRbPT01nk8mz/Okjhp10K87YDvZpbD736OyI3+bxpz3/qMVkjGrFH2YA0fLoPJrIbeD0diy0fnCF5okXNfwZYCkzXoaOGRdT48r3JSmBRDWUMLWmiW8Iri/YtLKAyzna0IZp7byR9io/pwyHB4VoSCx5iXNPPOMWG9D6uKVE1AoGBAINpI1yAr4WMWIXG7gnHRwWKFynimUC7nMFq6x9+hOUUxvAqpnvABGv0xnn3ir999ZpncG1ys6RpGMPm3NOs0nGXrQDgIzsc9FGzCIo54f9hLjyhE74zBAWapk+VTwzduJ0x0OBeCuQjSpUQbqCa+wiP+7oU3O5k9JN7KT4FF/fdAoGALZzcMAzzEATLzQbxN1j0PMbOlAjwQWurTG7ubYgFE+o06WPZdbXpBq2EZNcU4eI3BKv+jZ63yq2lREHXc0SOlvmxR3A+gLY2UD4ToV3iLbQsHVoPgoCCG7mmS041fg2v5K2ZS4sxrAoK/oBbn5NShK5JR6Q/9wA8B9tVph+nEdA="; // Replace with your base64-encoded private key

			// Load public and private keys
			PublicKey publicKey = getPublicKeyFromBase64(base64PublicKey);
			PrivateKey privateKey = getPrivateKeyFromBase64(base64PrivateKey);

			// Sample plaintext to encrypt
			String plaintext = "Hello, RSA Encryption and Decryption!";

			// Encrypt the data
			String encryptedText = encrypt(plaintext, publicKey);
			System.out.println("Encrypted Text: " + encryptedText);

			// Decrypt the data
			String decryptedText = decrypt(encryptedText, privateKey);
			System.out.println("Decrypted Text: " + decryptedText);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
