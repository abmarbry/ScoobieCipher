import java.io.IOException;
import java.math.BigInteger;

public class Scoober {
	private static final String[][] ScoobieWords = {
			{"Sc", "oobie"},					//	0
			{"Sh", "o-wop"},					//	1 
			{"Sa-shoobie-sc", "ah"},			//	2
			{"Ski-b" , "ee"},					//	3
			{"Bub", "ip"},						//	4
			{"Boo-", "op"},						//	5
			{"Bah-da-b" , "adda"}, 				//	6	
			{"Dad", "oo"},						//	7
			{"Doob" , "ub"},					//	8
			{"Du-", "oh"},						//  9
			{"Ah-r" , "a-pa-pa"},				//	A
			{"N", "a-ah"},						//	B
			{"R", "ootie-toot-toot"},			//	C
			{"Zoo-wow-k" , "opow"},			    //	D
			{"Cr", "eedie"},				   	//	E
			{"Y", "ip-a-dip"},				   	//	F
	};
	
	public static String Scoobify(byte[] b) {
		String hex = convertByteToHex(b);
		StringBuilder scoobieString = new StringBuilder();
		int pos;
		boolean firstWord = true;
		
		
		for(int i = 0; i < hex.length(); i++) {
			pos = Integer.parseInt(String.valueOf(hex.charAt(i)), 16);
			if(firstWord) {
				scoobieString.append(ScoobieWords[pos][0]);
				firstWord = false;
			}
			else {
				scoobieString.append(ScoobieWords[pos][1] + " ");
				firstWord = true;
			}
		}
			
		return scoobieString.toString();

	}
	
	public static byte[] deScoobify(String scoobie) throws IOException{
		StringBuilder removie = new StringBuilder(scoobie);
		StringBuilder deScoobed = new StringBuilder();
		String word;
		String firstPart;
		int firstPartLength = 0;
		String secondPart;
		boolean validScoobie = false;
		
		while(removie.length() > 0) {
			validScoobie = false;
			//Get the word index
			if(removie.indexOf(" ") != -1) {
				word = removie.substring(0, removie.indexOf(" "));
			}
			else {
				word = removie.substring(0);
			}
			
			//Find the corresponding position for the ScoobiePart and translate it into hex
			firstPart = word.substring(0, 1);
			if(firstPart.equals("S") || firstPart.equals("B") || firstPart.equals("D")) {
				firstPart = word.substring(0, 2);
			}
			for(int i = 0; i < ScoobieWords.length; i++) {
				if(ScoobieWords[i][0].indexOf(firstPart) == 0) {
					deScoobed.append(Integer.toHexString(i));
					firstPartLength = ScoobieWords[i][0].length();
					validScoobie = true;
					break;
				}
			}
			
			//Check to see if the code is a valid scoobified code. If not, it does not need to be descoobified.
			if(!validScoobie) {
				throw new IOException();
			}
			else {
				validScoobie = false;
			}
			
			//Same as above but for the second part
			if(word.charAt(word.length()-1) == '!') {
				secondPart = word.substring(firstPartLength, word.length()-1);
			}
			else {
				secondPart = word.substring(firstPartLength, word.length());
		}
			for(int j = 0; j < ScoobieWords.length; j++) {
				if(ScoobieWords[j][1].equals(secondPart)) {
					deScoobed.append(Integer.toHexString(j));
					validScoobie = true;
					break;
				}
			}
			
			if(!validScoobie) {
				throw new IOException();
			}
			else {
				validScoobie = false;
			}
			
			//Remove the word and start again
			
			removie = removie.delete(0, word.length());
			
			if(removie.length() > 0 && removie.charAt(0) == ' ') {
				removie = removie.deleteCharAt(0);
			}
		}
		if(deScoobed.length() > 0) {
		return new BigInteger(deScoobed.toString(), 16).toByteArray(); //Converts the hex string to an integer, and then to a byte
		}
		else {
			throw new IOException();
		}
	}
	
	public static String convertByteToHex(byte[] b) {
		StringBuilder temp = new StringBuilder();
		for(int i = 0; i < b.length; i++) {
			temp.append(String.format("%02X", b[i])); //02X, 0 = zero-padded, 2 = width, x = hexadecimal integer
		}
		return temp.toString();
	}
	
	public static String punctuateScoobieString(String s) {
		String temp = s.substring(0, s.length()-1); //ScoobieString will always end in a ' '
		return temp + "!";
		}
}