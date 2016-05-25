package HardCode;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import Common.absI18N;



//import misc.utility;



import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.firefox.FirefoxProfile;/

public class hardcode extends absI18N
{
	//define UI spy variables
	File file_hardcode = new File("./report_hardcode.txt"); 
	
	int hardcodescreen_id;
	int page_count= 0;
	int[] hardcode_count= new int[]{0};
	int[] string_count =  new int[]{0};	
	
	String hardcodeScreenPath = "./hardcode_screenshot/";
	 
	public void start(){
		//this.setPLOCSymbol("");
		detectHardcode(this.driver,this.symbol);
	}

	/*To check if have hardcode problem in current web page, and hardcode validator function begins from below function */

	public void detectHardcode(WebDriver driver, String puidmark) 
	{
		System.out.println("***************************************************");
		System.out.println("Hardcode Validator starts");
		System.out.println("***************************************************");
	    ArrayList<String> hardcodeLog = new ArrayList<String>();
		boolean context = this.extractContexts(driver, hardcodeLog, puidmark);
		
		if( context )
		{
			hardcodeLog = (ArrayList<String>)deduplicate(hardcodeLog);
			CaptureScreenShot();			
			
		}
	          
	}
	
	/*To extract all elements of current one web page, and assert if have harkcode problem. */

	boolean extractContexts(WebDriver driver, ArrayList<String> hardcodeLog, String puidmark) 
	{
		int seq = 0;
		boolean hardcodeExist = false;
        List<WebElement> elementsList = driver.findElements(By.cssSelector("body *"));
		//System.out.println("Nodes number in Total: " + elementsList.size());		 
		JavascriptExecutor js = (JavascriptExecutor) driver;
		boolean visible = false;		
		for(WebElement e: elementsList){
			try{
				/*check if element is visible*/
		        visible = e.isDisplayed(); 	 
//		        System.out.println("visible: " + visible);
		        /*filter out by visible and clickable*/
		        if(visible)
				{	                	
					/* detect hardcode strings in value*/
		            
		            String value = e.getText();
		            if(value == null || value.equals("")) value = e.getAttribute("value");
//		            System.out.println("Value: " + value);
		            
		            if(value != null)
					{
						value = value.trim();
						value = value.replaceAll("\t", "").trim();
						value = value.replaceAll("\r", " ").trim();
						value = value.replaceAll("\n", " ").trim();
						
						//System.out.println("----hardcode-----" + value + "-----");
						
		                boolean hardcode_value = this.assertPseudoHardcode(value, hardcodeLog, puidmark);
//		                System.out.println("hardcode: "+ hardcode_value);
	                	if(hardcode_value)
						{		
	                		hardcodeExist = true;
	                		highlightElementY(driver, e);
//	                		System.out.println("Highlight: "+ seq);
	                	}
		            }
		                		
	        		js.executeScript("arguments[0].setAttribute('loc_id', arguments[1]);",e, seq);
//	        		System.out.println("execute JS");
	        		//check if out of string under this node
	        		//String outstr = findTaglessString(driver, seq);
	        		//if(!outstr.equals("")){
	        			//boolean hardcode = this.assertPseudoHardcode(outstr, hardcodeLog, puidmark);
	        			//if(hardcode){	        				
	        				//hardcodeExist = true;
	        				//utility.highlightElementY(driver, e);
	        				
	        			//}
	        		//}
	        		    // handle dropdownlist 	
//	        		 try{
	        		   // this.captureDropdownMenu (driver, seq, puidmark);
//	        		 } catch (Exception exp){}
	        		 
	        		 
	        			boolean hasChildren = hasChild(driver, seq);
//	        			System.out.println("has childer: "+ hasChildren);
	        			/*if the node has no child*/
					
	        			if(!hasChildren)
	        			{
	        				String txtvalue = e.getText().trim();
	        				if(txtvalue==null | txtvalue.equals(""))
	        					txtvalue = e.getAttribute("value");
						
	                	
	        				boolean hardcode= this.assertPseudoHardcode(txtvalue, hardcodeLog, puidmark); 

	        				if(hardcode){
	        					hardcodeExist = true;
	        					highlightElementY(driver, e);
	        				}
	        			}
	        		
	        		
	        	}
              	seq++;
//              	System.out.println(seq);
			}
			catch(StaleElementReferenceException e2)
			{
				//e2.printStackTrace();
//				System.out.println("[Warning]: Error occurs when detecting element #" + seq);
				//utility.writeToLOG("[Warning]: Error occurs when detecting element #" + seq, "./error.log");
			}
			catch(WebDriverException e3)
			{
//				System.out.println("[Warning]: Error occurs when detecting element #" + seq);
				
				//utility.writeToLOG("[Warning]: Error occurs when detecting element #" + seq, "./error.log");
			}
              	
		}

		 return hardcodeExist;
		 
	 }
	
	/*to assert if the string has hardcode problem*/
	boolean assertPseudoHardcode(String label, ArrayList<String> hardcodeLog, String index){
		boolean hardcodeExist = false;
		if (label != null)
			if(!label.endsWith(index)){
				if(label.indexOf(index) !=-1)
					label = label.substring(label.lastIndexOf(index.substring(0, 1)) + 1, label.length());
				hardcodeExist = assertHardcode(label.trim(), hardcodeLog);
			}
				
		return hardcodeExist;

	}
	boolean assertHardcode(String label, ArrayList<String> hardcodeLog){
		boolean hardcodeExist = false;
		String copyright="Copyright \\? \\d{4} CA Technologies. All rights reserved.*";
        //check if it is copyright        
        if(!label.matches(copyright)) 
        	hardcodeExist = assertHardcode1(label, hardcodeLog);
        return hardcodeExist;
	}
    boolean assertHardcode1(String label, ArrayList<String> hardcodeLog)
    {
		
				ArrayList<String> termList = this.getTermlist();
                boolean hardcodeExist = false;
                boolean term=false;
                //String number = "[0-9]+(.[0-9]+)?(%)?";
                String number = "\\(?\\d+%?\\)?";
                //String english_string = "((\\pP)*)([a-zA-Z]+)(\\pP)*";    
                String english_string = "((\\pP)*)([a-zA-Z]+[-]?[a-zA-Z]+)(\\pP)*";                  
                
                System.out.println("---source----" + label + "-----");

                //split the string into single word*/          
                String[] split = Pattern.compile("\\s").split(label);
                
                String strENU = "";
                
                
                for(int j=0;j<split.length;j++)
                {           
                            //try to match termnology defined by user.                      
                            if (split[j].trim().equals("")) continue;
                            System.out.println("current string:"  + split[j]);
                            term = Matchterm(termList,split[j]);                                                          
                                                    
                            
                            if (term)
                                        continue;  
                            
                            /*if the single word is an English word and it is not a number*/
                            if(split[j].matches(english_string) && !split[j].matches(number))
                            {
                            
                                        strENU = strENU + " " + split[j];
                                        System.out.println("----enusstring -----" + strENU + "------------");
                                        term = Matchterm(termList,strENU.trim());
                                        		if(term){
								                            strENU="";
								                            hardcodeExist = false;                           
                                        					}
												else {
                                                     hardcodeExist = true;                           

														}                                                    

                            }
                            else
                            {
                                        if(!strENU.equals("")){                                                     
//                                                    String strTemp = "[Hard Code]:Find an English string [" +strENU.trim() +"] in [" + label.trim() + "]\r\n";
//                                                    System.out.println(strTemp);
//                                                    hardcodeLog.add(strTemp);
//                                                    strENU = "";
                                        	hardcodeExist = true;
                                                    break;
                                        	}
                            }
                }
                if(hardcodeExist){
                if(!strENU.equals("")){                                                     
                    String strTemp = "Find an English string [" +strENU.trim() +"] in [" + label.trim() + "]\r\n";
//                    System.out.println(strTemp);
                    hardcodeLog.add(strTemp);
                }              
        	}      
                System.out.println("************************"  + hardcodeExist);
                return hardcodeExist;
    }

	
	// delete the dedulicate string for hardcode. 
	public List<String> deduplicate(List<String> hardcode_list)
	{
		ArrayList<String> hardcode_dedup = new ArrayList<String>();
		if(hardcode_list.size() >0)
			hardcode_dedup.add(hardcode_list.get(0));
		for(int i=1;i<hardcode_list.size();i++)
		{
			boolean string_exist = false;
			for(int j=0;j<hardcode_dedup.size();j++)
			{
				if(hardcode_list.get(i).toString().equals(hardcode_dedup.get(j)))
				{
					string_exist  = true;
					break;
				}
				
				
			}
			if(!string_exist)
			{
				hardcode_dedup.add(hardcode_list.get(i));
			}
		}
		return hardcode_dedup;
	}
	// to confirm if the node has string which is out of each element
	public static String findTaglessString(WebDriver driver, int loc_id ) throws NoSuchElementException{
//      boolean hasChild = true;
//      System.out.println("Start finding tagless context");
		String result = "";
		
        String parent = "//body" + "//*[@loc_id=" + loc_id +"]";
        String child = "//body" + "//*[@loc_id=" + loc_id +"]/*";
        WebElement e = driver.findElement(By.xpath(parent));
        List<WebElement> children= driver.findElements(By.xpath(child));

        if(children.size() != 0){
        String subText="";
        String parentText = e.getText().trim();
        for(WebElement e2:children){
              subText =subText + e2.getText().trim() + "\n";
        }           
         String parentText2 = parentText.replaceAll("\r", "");
         String subText2 = subText.replaceAll("\r", "");
         parentText2 = parentText2.replaceAll("\n", "");
         subText2 = subText2.replaceAll("\n", "");
        if(parentText2.equals(subText2)){
        }
        else{
//            System.out.println("Node #" + loc_id + " has " + children.size() + " children");

              System.out.println(">>>>>>>>>>>Tagless string found in Node #"  + loc_id+ "<<<<<<<");
              result = removeDupStr(parentText2, subText);
     
        }
        }
        return result;
  }
	
	public static String removeDupStr(String str1_Parent, String str2_Children){
        String[] splitStr = str2_Children.split("\n");
        for(String s: splitStr){
//            System.out.println("[Removing: ]" + s);
              str1_Parent = str1_Parent.replace(s, "");
        }
//      System.out.println("Result: " + str1_Parent);
        return str1_Parent;
  }



	/*To check if current element is the leaf element */
	
	static boolean hasChild(WebDriver driver, int loc_id)
	 {
//		System.out.println("Start has child"); 
		boolean hasChild = true;
		//		 System.out.println("//body//*[@loc_id=" + loc_id +"]");
		 String child = "//body//*[@loc_id=" + loc_id +"]/*";
//		 System.out.println("child: "  + child);
//		 List<WebElement> children= driver.findElements(By.xpath(child));
		//			WebElement nodeToParent = driver.findElement(By.xpath("//body//*[@loc_id='" + loc_id + "']"));	

//				 System.out.println(children.size());
//		 if(children.size() == 0)
//		 {
//			 hasChild = false;
//		 }
		 hasChild = false;
//		 System.out.println("end: " + hasChild); 
		 return hasChild;
	 }

	
	
	
	public void captureDropdownMenu (WebDriver driver, int seq, int puidmark) throws Exception{
		 BufferedImage img;
		 BufferedImage img1;
		 ArrayList<String> logstr = new ArrayList<String>();
		 
		 String currentnode = "//body//*[@loc_id=" + seq +"]";	
		 String child = "//body" + "//*[@loc_id=" + seq +"]";
		 boolean hardcode= false;
		 WebElement parent = driver.findElement(By.xpath(currentnode));		 
		 
		 if (parent.getTagName().equals("select")) {
			// check if option contain hardcode 
		    
		    List<WebElement> children= driver.findElements(By.xpath(child));
		    for(WebElement e: children){
		    	String txtvalue = e.getText().trim();
				if(txtvalue==null | txtvalue.equals(""))
					txtvalue = e.getAttribute("value");			
        	
				//hardcode= this.assertPseudoHardcode(txtvalue, logstr, puidmark); 
				if(hardcode) break;
		    }
		    
		    if(hardcode){
			 // option has hardcode, begin to capture
			 parent.click();
//			 TextFormat.getScrn(driver)
//			 img1 = CaptureDropDown.TextFormat.getScrn(driver);
//			 img = CaptureDropDown.TextFormat.gsscAddTooltip(driver ,parent, img1, child);
			 SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss"); //create new image with drop down menu
			 format.setTimeZone(TimeZone.getTimeZone("GMT"));
			 Date date = new Date();
			  String strDate = format.format(date);
			  String PID = "00000";
			  String screenName = PID + "dropDownMenu" + strDate + "_" + seq + ".png";
			  String screenName_full =  screenName;
			  
//			  ImageIO.write((RenderedImage)img, "png", new File(hardcodeScreenPath  + screenName_full));
		    }
		}
	}
	
	String puidmark(int index){
		String mark = "]]";
		if(index ==1) mark = "))";
		if(index ==2) mark = ">>";
		return mark;
	}
	
	

	
	void highlightElement(WebDriver driver, String label)
	{
		ArrayList<WebElement> elements = this.getElements(driver, label);
		//System.out.println(elements.size());
		for(WebElement e: elements)
		{
			highlightElementY(driver, e);
		}				
			
	}

	// try to match Terminology defined by user (by default single char is included)
	
	boolean Matchterm( ArrayList<String> termList, String label){
		boolean MatchTerm = false;
		//single char, pass
		if(label.length()==1){
			MatchTerm = true;			
		}else
		{
			// try to match term list
			for(int k=0;k<termList.size();k++)
			{
				if(label.matches(termList.get(k).toString()))
				{
					MatchTerm = true;
					break;
				}
			}	
			if(!MatchTerm){
				//not match term, confirm if at the end have other character
//				System.out.println("debug" + label);
				String labeltemp = label.substring(0, label.length() -1);
				for(int k=0;k<termList.size();k++)
				{
					if(labeltemp.matches(termList.get(k).toString()))
					{
						MatchTerm = true;
						break;
					}
				}	
			}
			if(!MatchTerm){
				//not match term, confirm if at the begin have other character
				String labeltemp = label.substring(1, label.length());
				for(int k=0;k<termList.size();k++)
				{
					if(labeltemp.matches(termList.get(k).toString()))
					{
						MatchTerm = true;
						break;
					}
				}	
			}
		
			if(!MatchTerm){
				//not match term, confirm if at the begin and end have other character
				String labeltemp = label.substring(1, label.length() -1);
				for(int k=0;k<termList.size();k++)
				{
					if(labeltemp.matches(termList.get(k).toString()))
					{
						MatchTerm = true;
						break;
					}
				}	
			}
		}
		return MatchTerm;
	}

	/*read Terminology file defined by user, */
	 ArrayList<String> getTermlist() 
	{
		ArrayList<String> term_list = new ArrayList<String>();
		String text = new String();
		
		File file_term = new File("./Terminology.txt"); 
		try
		{
			if(file_term.exists())
			{
				BufferedReader br = new BufferedReader (new FileReader(file_term));
				while((text = br.readLine()) != null)  
				{
					text = text.trim();
					if (!text.equals("")){	
						term_list.add(text);						
					}
				}
				br.close();
			}
			else
			{
				//FileWriter fw = new FileWriter("./Terminology.txt");
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return term_list;
	}
	
	


	/*---------------------------------------------------------------------------------------------------------*/



	
	ArrayList<WebElement> findElements(WebDriver driver, List<WebElement> parent,String currentLevel, String Label, ArrayList<WebElement> elements)
	{
		
		int childrenNumber=0;
//		ArrayList<WebElement> elements = new ArrayList<WebElement>();
		   for(int i=0;i<parent.size();i++)
		   {
			   String text;
			   String nextLevel = currentLevel+ "[" + (i+1) + "]/*";
			   WebElement currentElement = driver.findElement(By.xpath(currentLevel+ "[" + (i+1) + "]"));
			   List<WebElement> children = driver.findElements(By.xpath(nextLevel));
			   childrenNumber = children.size();
			   if(childrenNumber !=0)
			   {
				   this.findElements(driver, children, nextLevel, Label, elements);
			   }
			   else
			   {
				   text = currentElement.getText();
				   if(text.equals(Label))
					   elements.add(currentElement);
			   }
		   }
		
		return elements;
	}
	

	
	ArrayList<WebElement> getElements(WebDriver driver, String Label)
	{
		
		ArrayList<WebElement> elements = new ArrayList<WebElement>();
		String xml_root = "//body/*";
		
		List<WebElement> allElement = driver.findElements(By.xpath(xml_root));
//		
      
		this.findElements(driver, allElement, xml_root, Label, elements);
		
		
		
		return elements;
	}
	
	ArrayList<String> filter(ArrayList<String> stringList)
	{
		ArrayList<String> stringFilter = new ArrayList<String>();
		ArrayList<String> term_lists = getTermlist();
		String number = "[0-9]+(.[0-9]+)?(%)?(:)?(.)?";
		boolean term = false;
		
		for(String e: stringList)
		{
			/*if the string is a number, skip it;*/
			if(e.trim().matches(number))
			{
				
				continue;
			}

			//if the string is blank, skip it;
			if(e.trim().equals(""))
				continue;	
			
			//if the string is a term, skip it;			
			for(int k=0;k<term_lists.size();k++)
			{
				if(e.trim().matches(term_lists.get(k).toString()))
				{
					//System.out.println("This is a Terminology, skip it!\n");
					term = true;
					break;
				}
			}
			if (term)
				continue;
			
			//if the string is not a number, not blank, not a term, add it to string list;
			stringFilter.add(e);
		}
		return stringFilter;
	}


	boolean detectValueHardcode(WebDriver driver) 
	{
//	    System.out.println("START VALUE");
		boolean hardcode_exist = false;

		String number = "[0-9]+(.[0-9]+)?(%)?";				
		String english_string = "(\\w+\\s)*\\w+(:)?(.)?";	
		
        List<WebElement> allElements = driver.findElements(By.cssSelector("body *"));

        for(WebElement e: allElements)
        {
        	String value = e.getAttribute("value");
        	if(value != null){
        		value = value.trim();
//        		 System.out.println("[INFO-ValueHardcode]: " + value);
        		if(value.matches(english_string) && !value.matches(number))
					{	  
        			highlightElementY(driver, e);
	        		  hardcode_exist = true;
	        		  
	        		}
        	}
		
        }
		return hardcode_exist;
	}
	
	 
	
	
}
