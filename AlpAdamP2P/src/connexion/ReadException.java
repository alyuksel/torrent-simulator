package connexion;

public class ReadException extends Exception{
	
	private static final long serialVersionUID = -4493516606099426865L;

	@Override
	public String getMessage() {
		return "read nothing";
	}
}
