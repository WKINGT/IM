package im.xgs.net.exception;

public class ImException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3271868948975160950L;
	
	private int code;
	private String message;
	
	public ImException(int code){
		this.code = code;
	}
	
	public ImException(int  code ,String message){
		super(message);
		this.code = code;
		this.message = message;
	}

	public int getCode(){
		return this.code;
	}
	
	public String getMsg(){
		return this.message;
	}
}
