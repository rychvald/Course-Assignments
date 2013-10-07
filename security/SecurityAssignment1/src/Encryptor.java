import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;


public class Encryptor {

	public String algorithm = "HmacMD5";
	private SecretKey secretkey = null;
	private Mac hmac = null;
	private File root = null;
	
	public Encryptor(String exceptions, File root, String password) {
		this.root = root;
		System.out.println(root.getAbsolutePath());
		this.createKeyFromString(password);	

		try {
			this.hmac = Mac.getInstance(this.algorithm);
			this.hmac.init(secretkey); 
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Error: No algorithm with the name" + algorithm + "found!");
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			System.out.println("Error: The key provided is not valid!");
			e.printStackTrace();
		}
	}
	
	private void createKeyFromString(String password) {
		byte[] keydata = password.getBytes();
		//secretkey = new SecretKeySpec(keydata, "HmacMD5");
		try {
			secretkey = KeyGenerator.getInstance(algorithm).generateKey();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void index() {
		this.index(root);
	}
	
	private void index(File dir) {
		File[] children = dir.listFiles();
		for (String s : dir.list()) {
			System.out.println(s);
		}
		PrintWriter out = null;
		
		try {
			File indexFile = new File(dir, ".cipherIndex.txt");
			out = new PrintWriter(new FileWriter(indexFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		for (File file : children) {
			if (file.isHidden()) {
				System.out.println("hidden file: " + file.getName());
				continue;
			}else if (file.isDirectory()) {
				this.index(file);
			}
			System.out.println(file.getName());
			byte[] ciphertext = this.encrypt(file);
			String line = new String(ciphertext);
			out.println(line);
		}
		out.close();
	}
	
	private byte[] encrypt (File f) {		
		byte[] ciphertext = null;
		
		try {
			int filesize = (int) f.length();
			byte[] data = new byte[filesize];
			DataInputStream in = new DataInputStream (new FileInputStream(f));
			in.readFully(data);
			in.close();
			
			ciphertext = hmac.doFinal(data);
			System.out.println(ciphertext);
		} catch (FileNotFoundException e) {
			System.out.println("The File provided does not exist!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("There was an eror reading the file!");
			e.printStackTrace();
		}
		
		return ciphertext;
	}
	
	public void analyze() {
		
	}
	
}
