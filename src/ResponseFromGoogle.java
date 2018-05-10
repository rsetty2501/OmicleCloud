

public class ResponseFromGoogle {
	
	
	private Result[] results;
	private String status;
	
	private String error_message;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Result[] getResults() {
		return results;
	}
	public void setResults(Result[] results) {
		this.results = results;
	}
	public String getError_message() {
		return error_message;
	}
	public void setError_message(String error_message) {
		this.error_message = error_message;
	}
	
	
	
	

}
