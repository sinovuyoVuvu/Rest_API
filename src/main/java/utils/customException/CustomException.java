package utils.customException;



public class CustomException extends Exception

{
    
	
	/**
	 * 
	 * Example of how to throw a custom exception
	 * 
	 * @param args
	 * @author Craig Adam - Modified 12-02-2020
	 * 
	 */
	public static void main(String[] args){

		try {

			throw new CustomException("match not found");

		} catch (CustomException ex) {
			
//			System.out.println("Caught the exception");
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			

		}

	}
	
	
	// Auto generated serial
	private static final long serialVersionUID = 7095091830224431476L;

	public CustomException(String exceptionMessage)
    {
        // Call constructor of parent Exception
        super(exceptionMessage);
    }
	
	/**
	 * 
	 * Throws exception with passed exception message
	 * 
	 * @param exceptionMessage : message to print to console
	 * @author Craig Adam - Modified 12-02-2020
	 * 
	 */	
	public static void throwException(String exceptionMessage) {
	
		try {

			throw new CustomException(exceptionMessage);

		} catch (CustomException ex) {

//			System.out.println("[FileFolderUtils findLastModified ln 82 ex.getMessage() = ] " + ex.getMessage());
			ex.printStackTrace();

		}
		
		
	}
	
	
	
} 