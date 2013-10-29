import java.math.BigInteger;
import java.util.BitSet;
import java.util.Random;


public class RSAGen {

	public int p , q , n , e , d;
	private int bottom = 1001 , top = 10000;
	private Random generator;
	
	public static void main(String[] args) {
		RSAGen rsa = new RSAGen();
		String text = "my";
		System.out.println("p: " + rsa.p);
		System.out.println("q: " + rsa.q);
		System.out.println("n: " + rsa.n);
		System.out.println("d: " + rsa.d);
		System.out.println("e: " + rsa.e);
		System.out.println("text: " + text);
		System.out.println("as bytes: " + RSAGen.byteString(text.getBytes()));
		byte[] ciphered = rsa.encrypt(text.getBytes());
		System.out.println("ciphered: " + RSAGen.byteString(ciphered));
		byte[] deciphered = RSAGen.decrypt(ciphered, rsa.d, rsa.n);
		System.out.println("deciphered: " + new String(deciphered));
	}
	
	public RSAGen() {
		this.generator = new Random();
		this.p = this.generatePrime(bottom, top);
		this.q = this.generatePrime(bottom, top);
		this.n = this.p * this.q;
		this.d = this.generateGreaterRelPrime(Math.max(this.p, this.q));
		this.e = this.multiplicativeInverse(this.p, this.q, this.d);
	}

	private int generatePrime(int bottom , int top) {
		int testNumber;
		do {
			testNumber = this.generator.nextInt(top-bottom) + bottom;
		} while(!this.isPrime(testNumber));
		return testNumber;
	}
	
	private int generateGreaterRelPrime(int number) {
		//any prime number greater that number will be relPrime
		int testNumber;
		do {
			testNumber = this.generator.nextInt(10000) + number;
		} while(!this.isPrime(testNumber));
		return testNumber;
	}
	
	private int multiplicativeInverse(int p , int q , int d) {
		int e , phi_n = (p-1)*(q-1);
		do {
			e = Math.abs(this.generator.nextInt());
		} while (((d * e) % phi_n) != 1);
		return e;
	}
	
	private boolean isPrime(int number) {
		boolean prime = true;
		for(int i=2 ; i < (number/2) ; i++) {
			if ((number%i) == 0) {
				prime = false;
			} else {
				continue;
			}
		}
		return prime;
	}
	
	public int getPublicKey() {
		return 0;
	}
	
	public byte[] encrypt(byte[] bytes) {
		return RSAGen.decrypt(bytes, this.e, this.n);
	}
	
	public static byte[] decrypt(byte[] bytes , int d , int n) {
		BigInteger integer = new BigInteger(bytes);
		System.out.println("integer: " + integer);
		BigInteger cipher = BigInteger.valueOf(1);
		BitSet bits = RSAGen.getBitSetfromInteger(d);
		for(int h = 0;h < bits.length();h++) {
			cipher = cipher.modPow(BigInteger.valueOf(2), BigInteger.valueOf(n));
			if( bits.get(h) ){
				cipher = cipher.multiply(integer);
				cipher = cipher.mod(BigInteger.valueOf(n));
			}
		}
		System.out.println("in decrypt function: " + cipher);
		return cipher.toByteArray();
	}
	
	public static int[] byteInt(byte[] bytes) {
		int[] integers = new int[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			integers[i] = (int)bytes[i];
		}
		return integers;
	}
	
	public static String byteString(byte[] bytes) {
		StringBuffer hash = new StringBuffer();
		String value;
		for (int i = 0; i < bytes.length; i++) {
			value = Integer.toString(bytes[i]);
			if (value.length() == 1) {
				hash.append('0');
			}
			hash.append(value);
		}
		return new String(hash);
	}
	
	    
	public static BitSet getBitSetfromInteger(int integer) {
		String tmp = Integer.toBinaryString(integer);
		BitSet bitSet = new BitSet(tmp.length());
		for (int i = 0; i < tmp.length(); i++) {
			Character tmpChar = tmp.charAt(i);
			if (tmpChar.compareTo('1') == 0)
				bitSet.set(i);
		}
		return bitSet;
	}
	
}
