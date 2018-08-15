package xyz.haojun0823.bn;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class Main {
	public static void main(String[] args) throws NumberFormatException, FileNotFoundException, IOException, Exception {
		
		if(args.length!=6) {
			
			System.err.println("Error:Wrong params number: "+Arrays.toString(args));
			pI();
			System.err.println("Faild.");
			return ;
		}
		
		if(args[0].toLowerCase().equals("-e")) {
			BufferedImage bi =ImageCryptor.encrpyt(args[1], Integer.valueOf(args[2]), Integer.valueOf(args[3]), ImageIO.read(new FileInputStream(args[4])));
			ImageIO.write(bi, args[4].substring(args[4].lastIndexOf('.')+1), new FileOutputStream(args[5]));
			System.out.println("done.");
			return ;
		}
		if(args[0].toLowerCase().equals("-d")) {
			BufferedImage bi =ImageCryptor.decrpyt(args[1], Integer.valueOf(args[2]), Integer.valueOf(args[3]), ImageIO.read(new FileInputStream(args[4])));
			ImageIO.write(bi, args[4].substring(args[4].lastIndexOf('.')+1), new FileOutputStream(args[5]));
			System.out.println("done.");
			return ;
		}
		
		
		System.err.println("Unknown error:"+Arrays.toString(args));
		return ;
		
		
	}
	
	
	
	public synchronized static void pI() {
		System.out.println("You need six params to do that:");
		System.out.println("[String]-e or -d:Encrypt or decrypt image.");
		System.out.println("[String]{password}:Encrypt or decrypt need a password.");
		System.out.println("[Integer]{blockWidth}:Set every little block width.");
		System.out.println("[Integer]{blockHeight}:Set every little block height.");
		System.out.println("[String]{originalImagePath}:Set original image path to load.");
		System.out.println("[String]{newImagePath}:Set new image path to save.");
		System.out.println("Please enter in order.");
	}
	
}
