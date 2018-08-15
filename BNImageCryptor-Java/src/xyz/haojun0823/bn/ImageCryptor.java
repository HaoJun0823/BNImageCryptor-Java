package xyz.haojun0823.bn;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * Copyright @ 2018 HaoJun0823.
 */
/**
 * @author HaoJun0823
 * @since JDK1.6
 * @version 1.0
 * @date 8/15/2018
 * @email dev@haojun0823.xyz
 */

public class ImageCryptor {

	private static int[] getKey(String password, int totalBlock) {
		int[] keyValue = new int[password.length()];
		for (int i = 0; i < keyValue.length; i++) {
			keyValue[i] = password.charAt(i) + password.charAt(keyValue.length - 1 - i) % totalBlock;
		}

		if (password.length() == totalBlock) {

			return getSumIndex(keyValue);
		}

		int g[] = new int[totalBlock];
		if (password.length() > totalBlock) {
			for (int i = 0; i < g.length; i++) {
				g[i] = keyValue[i] + keyValue[keyValue.length - 1 - i] % totalBlock;
			}

		} else {
			for (int i = 0; i < keyValue.length; i++) {
				g[i] = keyValue[i];
			}

			for (int i = keyValue.length; i < g.length; i++) {
				g[i] = g[Math.abs(i - keyValue.length) % g.length]
						+ g[Math.abs(keyValue.length - (i - keyValue.length)) % g.length] % totalBlock;
			}

		}

		return getSumIndex(g);

	}

	private static int[] getSumIndex(int[] keyValue) {
		for (int i = 0; i < keyValue.length; i++) {
			keyValue[i] = Math.abs(keyValue[i] + (i % 2 == 0 ? i : -i));
		}
		return keyValue;
	}

	private static int getTotalBlock(int w, int h, int bw, int bh) throws Exception {
		
		if((double)w%bw!=0) {
			throw new Exception(String.format("%d mod %d!=0", w,bw));
		}
		if((double)h%bh!=0) {
			throw new Exception(String.format("%d mod %d!=0", h,bh));
		}
		
		bw = w / bw;
		bh = h / bh;
		return bw * bh;
	}

	private static int[] getIndex(int okey[], int nkey[]) {

		int[] index = new int[okey.length];
		for (int i = 0; i < okey.length; i++) {
			for (int x = 0; x < nkey.length; x++) {
				if (okey[i] == nkey[x]) {
					index[i] = x;
					nkey[x] = -1;
					break;
				}
			}

		}

		return index;

	}
	
	public static long timeCalc(long time) {
		return System.currentTimeMillis()-time;
	}

	public static BufferedImage encrpyt(String password, int blockWidth, int blockHeight, BufferedImage originalImage) throws Exception {
		
		long startTime = System.currentTimeMillis();
		System.out.printf("[%d]Start encrypt:%s\n",timeCalc(startTime),password);
		int totalBlock = getTotalBlock(originalImage.getWidth(), originalImage.getHeight(), blockWidth, blockHeight);
		 System.out.printf("[%d]Image Width:%d Height:%d\n",timeCalc(startTime), originalImage.getWidth(),originalImage.getHeight());
		 System.out.printf("[%d]Block Width:%d Height:%d\n",timeCalc(startTime), blockWidth,blockHeight);

		BufferedImage newImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
				originalImage.getType());
		int[] okey = getKey(password, totalBlock);
		int[] nkey = okey.clone();
		Arrays.sort(nkey);

		System.out.printf("[%d]Old Key:%s\n",timeCalc(startTime), Arrays.toString(okey));
		System.out.printf("[%d]New Key:%s\n",timeCalc(startTime), Arrays.toString(nkey));

		int[] index = getIndex(okey, nkey);

		System.out.printf("[%d]Encrypt Block Address:%s\n",timeCalc(startTime), Arrays.toString(index));

		int x = 0, y = 0;
		Graphics g = newImage.getGraphics();
		// g.setColor(new Color(0, 0, 0));

		for (int i = 0; i < index.length; i++) {

			BufferedImage buffImg = originalImage.getSubimage(x, y, blockWidth, blockHeight);

			int nx = index[i] % (originalImage.getWidth() / blockWidth);
			int ny = index[i] / (originalImage.getWidth() / blockWidth);

			nx *= blockWidth;
			ny *= blockHeight;

			System.out.printf("[%d]Copy X:%d Y:%d ",timeCalc(startTime), x, y);

			System.out.printf("[%d]From %d To %d X:%d Y:%d\n",timeCalc(startTime), i, index[i], nx, ny);
			g.drawImage(buffImg, nx, ny, null);
			// g.drawString(Integer.toString(index[i]), nx+10, ny+10);

			x = x + blockWidth;
			if (x == originalImage.getWidth()) {
				y = y + blockHeight;
				x = 0;
			}

		}
		System.out.printf("Finish! Total Time:%d\n",timeCalc(startTime));
		return newImage;
	}

	public static BufferedImage decrpyt(String password, int blockWidth, int blockHeight, BufferedImage originalImage) throws Exception {
		
		long startTime = System.currentTimeMillis();
		System.out.printf("[%d]Start decrypt:%s\n",timeCalc(startTime),password);
		int totalBlock = getTotalBlock(originalImage.getWidth(), originalImage.getHeight(), blockWidth, blockHeight);
		 System.out.printf("[%d]Image Width:%d Height:%d\n",timeCalc(startTime), originalImage.getWidth(),originalImage.getHeight());
		 System.out.printf("[%d]Block Width:%d Height:%d\n",timeCalc(startTime), blockWidth,blockHeight);

		BufferedImage newImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
				originalImage.getType());
		int[] okey = getKey(password, totalBlock);
		int[] nkey = okey.clone();
		Arrays.sort(nkey);

		 System.out.printf("[%d]Old Key:%s\n",timeCalc(startTime), Arrays.toString(okey));
		 System.out.printf("[%d]New Key:%s\n",timeCalc(startTime), Arrays.toString(nkey));

		int[] index = getIndex(okey, nkey);

		 System.out.printf("[%d]Decrypt Block Address:%s\n",timeCalc(startTime), Arrays.toString(index));

		int x = 0, y = 0;
		Graphics g = newImage.getGraphics();
		// g.setColor(new Color(0, 0, 0));

		for (int i = 0; i < index.length; i++) {

			int nx = index[i] % (originalImage.getWidth() / blockWidth);
			int ny = index[i] / (originalImage.getWidth() / blockWidth);

			nx *= blockWidth;
			ny *= blockHeight;

			BufferedImage buffImg = originalImage.getSubimage(nx, ny, blockWidth, blockHeight);

			System.out.printf("[%d]Copy X:%d Y:%d ",timeCalc(startTime), x, y);

			System.out.printf("[%d]From %d To %d X:%d Y:%d\n",timeCalc(startTime), i, index[i], nx, ny);
			g.drawImage(buffImg, x, y, null);
			// g.drawString(Integer.toString(index[i]), nx+10, ny+10);

			x = x + blockWidth;
			if (x == originalImage.getWidth()) {
				y = y + blockHeight;
				x = 0;
			}

		}
		System.out.printf("Finish! Total Time:%d\n",timeCalc(startTime));
		return newImage;
	}

}
