import java.util.Random;


public class RSAGen {

	public int p , q , n , e , d;
	private int bottom = 1001 , top = 10000;
	private Random generator;
	
	public static void main(String[] args) {
		RSAGen rsa = new RSAGen();
		String text = "myText";
		System.out.println("p: " + rsa.p);
		System.out.println("q: " + rsa.q);
		System.out.println("n: " + rsa.n);
		System.out.println("d: " + rsa.d);
		System.out.println("e: " + rsa.e);
		System.out.println("text: " + text);
		System.out.println("as bytes: " + text);
		System.out.println("ciphered: " + text);
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
	
	private int gcd (int a , int b) {
		//using a variation of euclid's algorithm defined in RSA paper
		int x_i = a, x_k = b , tmp;
		do {
			tmp = x_k;
			x_k = euclidRecursive(x_i , x_k);
			x_i = tmp;
		} while (x_k != 0);
		return x_i;
	}
	
	private int euclidRecursive(int x , int y) {
		return x%y;
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
	
	public Long getPublicKey() {
		return null;
	}
	
	public void encrypt() {
		
	}
	
	public static void decrypt() {
		
	}
	
}
