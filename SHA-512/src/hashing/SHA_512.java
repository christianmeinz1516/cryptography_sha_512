package hashing;

import java.math.BigInteger;

/**
 * 
 * @author meinzecp
 *
 * This class creates the actual SHA_512 hash and returns it in a hexadecimal string.
 * 
 * 
 * Simple helper methods are used for conversion and padding. The pad() function will
 * provide the extended bits to the hashing method discussed in class. Since the keys
 * are provided as an array, toMatrix() converts the keys into a block matrix. The 
 * getMessages() creates a block matrix to contain the different words produced from the
 * schedule.
 *
 */
public class SHA_512 {
	
	protected static String createHash(byte[] input) {
		
		// Set up the various blocks used throughout the SHA algorithm
		byte[] padded = pad(input);
		long[] currentHash = Tables.INITIAL_VALUE;
		long[][] matrix = toMatrix(padded);
		long[][] word = getMessages(matrix);

		// This loop is used to iterate through all of the message blocks created
		// after converting the hash to a matrix
		for (int i = 0; i < matrix.length; i++) {
		
			long a = currentHash[0];
			long b = currentHash[1];
			long c = currentHash[2];
			long d = currentHash[3];
			long e = currentHash[4];
			long f = currentHash[5];
			long g = currentHash[6];
			long h = currentHash[7];
		
			// This starts the 80-round function
			for (int j = 0; j < 80; j++) {
			
				// Need temporary values because 'a' and 'e' are changed
				// before the other values due to the round function
				long tempA = a;
				long tempE = e;				
				
				// Definitions for 'a' and 'e' given on SHA reference sheet
				// on Moodle
				a = 	h + 
						RoundFunctions.ch(e,f,g) + 
						RoundFunctions.sum1(e) +
						word[i][j] + 
						Tables.KEY[j] +
						RoundFunctions.sum0(a) + 
						RoundFunctions.maj(a,b,c);
				
				e = 	d + h + 
						RoundFunctions.ch(e,f,g) + 
						RoundFunctions.sum1(e) +
						word[i][j] + 
						Tables.KEY[j];
				
				// Update in reverse order so that the values don't end up
				// copying each other
				h = g;
				g = f;
				f = tempE;
				d = c;
				c = b;
				b = tempA;
			}
			
			// Complete the last step of the compression function
			// (i.e. adding to their previous values)
			currentHash[0] = a + currentHash[0];
			currentHash[1] = b + currentHash[1];
			currentHash[2] = c + currentHash[2];
			currentHash[3] = d + currentHash[3];
			currentHash[4] = e + currentHash[4];
			currentHash[5] = f + currentHash[5];
			currentHash[6] = g + currentHash[6];
			currentHash[7] = h + currentHash[7];
		}
			
		// convert output to a string
		String result = "";
		for (int i = 0; i < 8; i++) {
			result += String.format("%016x", currentHash[i]);
		}
		
		return result;
	}

	/**
	 * Creates a block matrix for the different words formed by the scheduling
	 * algorithm.
	 */
	private static long[][] getMessages(long[][] schedule) {
		long[][] newWord = new long[schedule.length][80];

		for (int i = 0; i < schedule.length; i++) {
			
			// Initial 15 values are stored
			for (int j = 0; j < 16; j++) {
				newWord[i][j] = schedule[i][j];								
			}

			// The rest of the 80 values are stored
			for (int k = 16; k < 80; k++) {									
				newWord[i][k] = RoundFunctions.sig1(newWord[i][k-2]) + 
								newWord[i][k-7] + 
								RoundFunctions.sig0(newWord[i][(k-16) + 1]) + 
								newWord[i][k-16];
			}
		}
		return newWord;
	}

	/**
	 * Converts the key table given on Moodle into a block formation. This is 
	 * done after the input has already been padded.
	 */
	private static long[][] toMatrix(byte[] paddedInput) {
		long[][] matrix = new long[paddedInput.length/128][16];
		
		for (int i = 0; i < (paddedInput.length/128); i++) {
			for (int j = 0; j < 16; j++) {
				long temp = 0;
				
				for (int k = 0; k < 8; k++) {
					temp = (paddedInput[k + i*128 + j*8] & 255) + (temp << 8);
				}
				matrix[i][j] = temp;
			}
		}
		return matrix;
	}
	
	/**
	 * Gives the extra padding needed to extend the hash
	 */
	private static byte[] pad(byte[] input) {
		int size = input.length + 17;
		
		// Convert to a byte array
		byte[] byteArray = BigInteger.valueOf(input.length * 8).toByteArray();		
		
		while (size % 128 != 0) {size += 1;}
		
		byte[] output = new byte[size];
		
		for (int i = 0; i < input.length; i++) {
			output[i] = input[i];
		}
		
		// Add the extra bit
		output[input.length] = (byte) 128;											

		// Place the byteArray at the end of the output
		for (int i = byteArray.length; i > 0; i--) {
			output[size-i] = byteArray[byteArray.length-i];
		}
		
		return output;
	}
}
