package pt.chat.crypto.axolotl;

public class CryptoFailedException extends Exception {
	public CryptoFailedException(Exception e){
		super(e);
	}
}
