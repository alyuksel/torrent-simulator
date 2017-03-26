package TP3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Exo1 {
	private BouncyCastleProvider bc = new BouncyCastleProvider();
	private FileInputStream fis;
	private FileOutputStream fos;
	private byte[] message;
	private byte[] hache ;
	private MessageDigest md;
	
	public Exo1() {
		Security.addProvider(bc);
		try {
			fis = new FileInputStream(new File("sha1/message.txt"));
			fos = new FileOutputStream(new File("sha1/hache.txt"));
			message = new byte[fis.available()];
			md = MessageDigest.getInstance("SHA-1",bc);
		} catch (IOException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		
	}
	public boolean verifHash(){
		boolean equal = false;
		try {
			FileInputStream fis = new FileInputStream(new File("sha1/hache.txt"));
			byte [] hash = new byte[fis.available()];
			fis.read(hash);
			equal = MessageDigest.isEqual(this.hache, hash);
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return equal;
	}
	public void hach(){
		try {
			fis.read(message);
			System.out.println("lecture du fichier");
			md.update(message);
			System.out.println("hachage du text");
			hache = md.digest();
			System.out.println("Fin d'hachage  ");
			fos.write(hache);
			System.out.println("ecriture du hachage dans un autre fichier");
			fis.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Exo1 e = new Exo1();
		e.hach();
		System.out.println("Correspondance : " +e.verifHash());
		
	}
}
