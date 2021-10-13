package hashing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Run_SHA_512 {

	public static void main(String[] args) throws IOException {

		File file = new File("shainput.txt");
		if(file.exists()) {
			System.out.println("The SHA encryption for 'shainput.txt' is:");
			FileInputStream inputStream = new FileInputStream(file);
			
			byte[] inputBytes = new byte[(int) file.length()];

			inputStream.read(inputBytes);
			String input = new String(inputBytes, "UTF-8");
			input = input.replaceAll("\r\n", "\n");

			StringBuilder encrypt = new StringBuilder(SHA_512.createHash(input.getBytes()));
			System.out.println(encrypt);

			inputStream.close();

		} else {
			System.out.print("File name 'shainput.txt' was not found."
					+ "\nPlease manually enter a valid filename: ");
			Scanner sc = new Scanner(System.in);
			String str = sc.nextLine();
			sc.close();
			file = new File(str);
			
			try {
				FileInputStream inputStream = new FileInputStream(file);
				byte[] inputBytes = new byte[(int) file.length()];

				inputStream.read(inputBytes);
				String input = new String(inputBytes, "UTF-8");
				input = input.replaceAll("\r\n", "\n");

				StringBuilder encrypt = new StringBuilder(SHA_512.createHash(input.getBytes()));
				System.out.println(encrypt);

				inputStream.close();
			}
			catch(Exception e) {
				System.out.println("\nERROR: =======================================\n"
						+ "The file you have typed is not found.\n"
						+ "Re-run the program and use a different filename.");
				return;
			}
		}
	}
}
