package com.eighti.onebkk.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageResizer {

	private static final Logger LOG = LoggerFactory.getLogger(ImageResizer.class);

	public static String resizeImageExceedsLimit(String base64Image) {
		// Decode the base64 string to byte array
		byte[] imageData = Base64.getDecoder().decode(base64Image);

		// Check if the image size is exceeds 2MB
		if (imageData.length > 2 * 1024 * 1024) { // 2MB in bytes
			LOG.info("Image size exceeds 2MB");
			// Resize the image to fit within 2MB size limit
			try {
				LOG.info("Convert the byte away to buffered image...");
				// Convert byte array to BufferedImage
				ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
				BufferedImage originalImage = ImageIO.read(bis);

				// Resize the image to reduce file size
				BufferedImage resizedImage = resizeImage(originalImage);

				// Convert resized image to Base64 string
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ImageIO.write(resizedImage, "jpg", bos);
				byte[] resizedImageData = bos.toByteArray();
				String resizedBase64Image = Base64.getEncoder().encodeToString(resizedImageData);

				return resizedBase64Image;

			} catch (Exception e) {
				e.printStackTrace();
				LOG.error(base64Image);
				return null; // Return null if resizing fails
			}
		} else {
			// Image size is within 2MB limit, return original Base64 image string
			return base64Image;
		}
	}

	// Method to resize image while maintaining aspect ratio
	private static BufferedImage resizeImage(BufferedImage originalImage) {
		LOG.info("Image resizing...");
		// Calculate new dimensions to fit within 2MB size limits
		int newWidth = originalImage.getWidth();
		int newHeight = originalImage.getHeight();

		double scaleFactor = Math.sqrt(2.0 * 1024 * 1024 / originalImage.getData().getDataBuffer().getSize());
		if (scaleFactor < 1) {
			newWidth = (int) (originalImage.getWidth() * scaleFactor);
			newHeight = (int) (originalImage.getHeight() * scaleFactor);
		}

		// Resize the image
		Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
		BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		resizedImage.getGraphics().drawImage(scaledImage, 0, 0, null);

		return resizedImage;
	}

	public static String resizeImageResolution(String base64ImageString, int maxWidth, int maxHeight) {
		try {
			// Decode base64 string to byte array
			byte[] imageBytes = Base64.getDecoder().decode(base64ImageString);

			// Convert byte array to BufferedImage
			ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
			BufferedImage originalImage = ImageIO.read(bis);

			// Resize the image
			BufferedImage resizedImage = resizeImage(originalImage, maxWidth, maxHeight);

			// Convert resized image to Base64 string
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(resizedImage, "png", bos);
			byte[] resizedImageData = bos.toByteArray();
			String resizedBase64Image = Base64.getEncoder().encodeToString(resizedImageData);

			return resizedBase64Image;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method to resize image while maintaining aspect ratio and limiting dimensions
	private static BufferedImage resizeImage(BufferedImage originalImage, int maxWidth, int maxHeight) {
		// Get original dimensions
		int originalWidth = originalImage.getWidth();
		int originalHeight = originalImage.getHeight();

		// Calculate new dimensions while maintaining aspect ratio
		int newWidth = originalWidth;
		int newHeight = originalHeight;

		// Check if width exceeds maximum
		if (newWidth > maxWidth) {
			newWidth = maxWidth;
			newHeight = (int) Math.round((double) newWidth / originalWidth * originalHeight);
		}

		// Check if height exceeds maximum
		if (newHeight > maxHeight) {
			newHeight = maxHeight;
			newWidth = (int) Math.round((double) newHeight / originalHeight * originalWidth);
		}

		// Resize the image
		Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
		BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		resizedImage.getGraphics().drawImage(scaledImage, 0, 0, null);

		return resizedImage;
	}
}
