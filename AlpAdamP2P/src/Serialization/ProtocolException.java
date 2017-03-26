package Serialization;

public class ProtocolException extends Exception {
	private static final long serialVersionUID = 7871092676161475732L;

	@Override
	public String getMessage() {
		return "Eror Protocol";
	}
}
