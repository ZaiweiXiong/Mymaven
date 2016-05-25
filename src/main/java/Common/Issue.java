package Common;

import java.io.Serializable;

public class Issue implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String Description;
	public String Title;
	public String ScreenShot;
	public String Category;
	public Issue(){
		
	}
	public Issue(String category){
		this.Category = category;
	}
	
}
