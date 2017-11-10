import java.math.BigInteger;
import java.util.Random;

public class RSA {
	private final int bitLength = 1024;
	private final BigInteger bigOne = BigInteger.ONE;
	
	//Product
	private BigInteger p;
	//Quotient
	private BigInteger q;
	//Product * Quotient
	private BigInteger n;
	//(Product-1) * (Quotient-1)
	private BigInteger totient;
	//Mod of Non-Encrypted Message
	private BigInteger e;
	//Mod of Encrypted Message
	private BigInteger d;
	
	private Random random;

	public RSA() {
		random = new Random();
		generateRSAValues(random);
	}
	
	private void generateRSAValues(Random random){
		p = BigInteger.probablePrime(bitLength/2, random);
		q = BigInteger.probablePrime(bitLength/2, random);
		n = p.multiply(q);
		totient = p.subtract(bigOne).multiply(q.subtract(bigOne)); //(p-1)*(q-1)
		e = new BigInteger("65537"); //2^16 + 1
		d = e.modInverse(totient);
	}
	
	public byte[] encrypt(String message) {
		byte[] messageByte = (message + " END").getBytes(); //for very short messages
		BigInteger encrypt = new BigInteger(messageByte).modPow(e, n);
		return encrypt.toByteArray();
	}
	
	public String decrypt(byte[] cryptedMessage) {
		BigInteger decrypt = new BigInteger(cryptedMessage).modPow(d, n);
		byte[] messageByte = decrypt.toByteArray();
		String s = new String(messageByte);
		return s.substring(0, s.length()-3);
	}
}
