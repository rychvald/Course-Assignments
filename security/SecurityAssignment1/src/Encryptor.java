import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;


public class Encryptor {

	public String algorithm = "HmacMD5";
	private SecretKeySpec secretkey = null;
	private File root = null;
	private String cipherFile = ".cipherIndex.txt";
	private String indexFile = ".fileIndex.txt";
	
	public Encryptor(String exceptions, File root, String password) {
		this.root = root;
		System.out.println(root.getAbsolutePath());
		this.createKeyFromString(password);	

	}
	
	private void createKeyFromString(String password) {
		byte[] keydata;
		try {
			keydata = password.getBytes("UTF-8");
			secretkey = new SecretKeySpec(keydata, this.algorithm);
		} catch (UnsupportedEncodingException e) {
			System.out.println("Problem with encoding!");
			e.printStackTrace();
		}
		//secretkey = new SecretKeySpec(keydata, "HmacMD5");
	}
	
	public void index() {
		this.index(root);
	}
	
	private void index(File dir) {
		assert(dir.isDirectory());
		File[] children = dir.listFiles();

		PrintWriter indexout = null , cipherout = null;
		
		try {
			File indexFile = new File(dir, this.cipherFile);
			cipherout = new PrintWriter(new FileWriter(indexFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			File indexFile = new File(dir, this.indexFile);
			indexout = new PrintWriter(new FileWriter(indexFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		for (File file : children) {
			if (file.isHidden()) {
				continue;
			}else if (file.isDirectory()) {
				this.index(file);
			}
			byte[] ciphertext = this.encrypt(file);
			cipherout.println(ciphertext);
			indexout.println(file.getName());
		}
		cipherout.close();
		indexout.close();
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
			System.out.println("OldHash:" + ciphertext);
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
	
	//generates a String with the contents of the CipherFile in the directory dir
	private void analyze(File dir) {
		assert(dir.isDirectory());
		HashSet<String> children = this.currentChildren(dir);
		HashSet<String> previousChildren = this.previousChildren(dir);
		HashSet<byte[]> previousCiphers = this.previousCiphers(dir);
		HashSet<String> modified, deleted, newFiles ;

		deleted = new HashSet<String>(previousChildren);
		deleted.removeAll(children);
		
		newFiles = new HashSet<String>(children);
		newFiles.removeAll(previousChildren);
		
		modified = new HashSet<String>(previousChildren);
		modified.retainAll(children);
		modified = modifiedFiles (dir , modified , previousCiphers);
		
		System.out.println("Previous Children:");
		System.out.println(previousChildren.toString());
		
		System.out.println("Current Children:");
		System.out.println(children.toString());
		
		System.out.println("Files deleted:");
		System.out.println(deleted.toString());
		
		System.out.println("Files added:");
		System.out.println(newFiles.toString());
		
		System.out.println("Files modified:");
		System.out.println(modified.toString());
		
		for (File f : dir.listFiles()) {
			if ( f.isDirectory()) {
				this.analyze(f);
			}
		}
		
	}
	
	//generates a String with one (not hidden) file per line contained in the directory given
		private HashSet<String> currentChildren (File dir) {
			assert(dir.isDirectory());
			File[] children = dir.listFiles();
			HashSet<String> currentChildren  = new HashSet<String>();
			
			for (File file : children) {
				if (file.isHidden()) {
					continue;
				}else {
					currentChildren.add(file.getName());
				}
			}
			
			return currentChildren;
		}
	
	//generates a String with the contents of the indexFile in the directory dir
	private HashSet<String> previousChildren (File dir) {
		assert(dir.isDirectory());
		HashSet<String> previousChildren = new HashSet<String>();
		String cipherContents = this.indexContents(dir, this.indexFile);
		
		for ( String filename : cipherContents.split("\n") ) {
			previousChildren.add(filename);
		}
		
		return previousChildren;
	}
	
	//generates a String with the contents of the CipherFile in the directory dir
	private HashSet<byte[]> previousCiphers (File dir) {
		HashSet<byte[]> previousCiphers = new HashSet<byte[]>();
		String cipherContents = this.indexContents(dir , this.cipherFile);
		
		for ( String cipher : cipherContents.split("\n") ) {
			previousCiphers.add(cipher.getBytes());
		}
		
		return previousCiphers;
	}
	
	//generates a String with the contents of the CipherFile in the directory dir
	private String indexContents (File dir , String index) {
		String filename = null;
		String lines = "";
		
		try {
			filename = dir.getCanonicalPath() + File.separator + index;
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String line;
			while((line = in.readLine()) != null) {
				lines = new String(lines + line + "\n");
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return lines;
	}
	
	//checks whether the current ciphers are contained in the previous ciphers
	private HashSet<String> modifiedFiles (File dir , HashSet<String> files , HashSet<byte[]> previousCiphers) {
		HashSet <String> modified = new HashSet<String>();
		
		for ( String s : files) {
			File f = new File(dir.getAbsolutePath(),s);
			byte[] hash = this.encrypt(f);
			System.out.println("Current Hash:" + hash);
			if (!previousCiphers.contains(hash.toString())) {
				modified.add(s);
			} 
		}
		return modified;
	}
	
}
