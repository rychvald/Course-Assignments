import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;


public class Encryptor {

	public String algorithm = "HmacMD5";
	private SecretKey secretkey = null;
	private File root = null;
	private String sep = ",";
	private String cipherFile = ".cipherIndex.txt";
	
	public Encryptor(String exceptions, File root, String password) {
		this.root = root;
		System.out.println(root.getAbsolutePath());
		this.createKeyFromString(password);	

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
			File indexFile = new File(dir, this.cipherFile);
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
			String line = file.getName() + this.sep + new String(ciphertext);
			out.println(line);
		}
		out.close();
	}
	
	private byte[] encrypt (File f) {		
		byte[] ciphertext = null;
		Mac hmac = null;
		
		try {
			int filesize = (int) f.length();
			byte[] data = new byte[filesize];
			DataInputStream in = new DataInputStream (new FileInputStream(f));
			in.readFully(data);
			in.close();
			hmac = Mac.getInstance(this.algorithm);
			hmac.init(this.secretkey);
			ciphertext = hmac.doFinal(data);
			System.out.print(ciphertext);
		} catch (FileNotFoundException e) {
			System.out.println("The File provided does not exist!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("There was an eror reading the file!");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Error: No algorithm with the name" + algorithm + "found!");
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			System.out.println("Error: The key provided is not valid!");
			e.printStackTrace();
		}
		
		return ciphertext;
	}
	
	public void analyze() {
		this.analyze(root);
	}
	
	private void analyze(File dir) {
		Set<String> children = new HashSet<String>(Arrays.asList(dir.list()));
		Set<String> previousChildren = this.previousChildren(dir);
		Set<String> modified = new HashSet<String>(), deleted, newFiles = new HashSet<String>();
		
		Set<String> previousCiphers = this.previousCiphers(dir);
		
		for ( String child : children) {
			if ( previousChildren.contains(child)) {
				previousChildren.remove(child);
			} else if ( !previousChildren.contains(child) ) {
				newFiles.add(child);
			}
		} //now, previousChildren contains but the deleted files and newFiles all the new files
		
		deleted = new HashSet<String>(previousChildren);
		
		
		
	}
	
	private HashSet<String> previousChildren (File dir) {
		HashSet<String> previousChildren = new HashSet<String>();
		String cipherContents = this.cipherContents(dir);
		
		for ( String line : cipherContents.split("\n") ) {
			String filename = line.split(this.sep)[0];
			previousChildren.add(filename);
		}
		
		return previousChildren;
	}
	
	private HashSet<String> previousCiphers (File dir) {
		HashSet<String> previousCiphers = new HashSet<String>();
		String cipherContents = this.cipherContents(dir);
		
		for ( String line : cipherContents.split("\n") ) {
			String filename = line.split(this.sep)[1];
			previousCiphers.add(filename);
		}
		
		return previousCiphers;
	}
	
	private String cipherContents (File dir) {
		String filename = null;
		String lines = null;
		
		try {
			filename = dir.getCanonicalPath() + File.separator + this.cipherFile;
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String line;
			while((line = in.readLine()) != null) {
				lines = new String(lines + line);
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return lines;
	}
	
}
