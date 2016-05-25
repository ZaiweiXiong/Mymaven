package iCurrency;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import Common.absI18N;

public class iCurrency extends absI18N {
	
	static int issueNumber = 0;
	static String language;
	static LinkedHashMap <String, Set<String>> map;
	
	
	public iCurrency (String langauge) {
		
		iCurrency.language= langauge;
		
	}
public void start()
	
	{
		System.out.println("***************************************************");
		System.out.println("iNumber Validator 1.0 starts");
		System.out.println("***************************************************");
	    ArrayList<String> iformateLog = new ArrayList<String>();
		boolean context = extractContexts(this.driver, iformateLog);
		if( context )
		{
			this.CaptureScreenShot();					
			
		}
		
		System.out.println("going through for iFormat is over");
	    
	}
static boolean hasChild(WebDriver driver, int loc_id)
{
	 boolean hasChild = true;

	 String child = "//body//*[@loc_id=" + loc_id +"]/*";
	 List<WebElement> children= driver.findElements(By.xpath(child));

	 if(children.size() == 0)
	 {
		 hasChild = false;
	 }
	 return hasChild;
}


public static ArrayList<String> getCurrencyFormatList(){
	
			//ENU: [$]\\b.*"+"\\b";       $123,456,789.00;
			//KOR  "^[₩]"+".";            ₩123,456,789;
			//GER: "^[0-9]"+".*"+"€"+"$"  123.456.789,00 €;
			//JPN: "^[¥]"+".";			  ¥123,456,789;
			//ITA: "^[€]"+".";            € 123.456.789,00;
			//FRA: "^[0-9]"+".*"+"€"+"$"; 123 456 789,00 €;
			//CHT: "^[NT$]"+".*";         NT$123,456,789.00;
	 		ArrayList<String> currencyFormatList = new ArrayList<String>();
	 		String currencyFormat=""; 
	 		currencyFormat ="JPN"+"_"+"^[¥]"+".";
	 		currencyFormatList.add(currencyFormat); //JPN,KRO,CHS,CHT,ENU
	 		currencyFormat ="KOR"+"_"+"^[₩]"+".";
	 		currencyFormatList.add(currencyFormat);
	 		currencyFormat ="GER"+"_"+ "^[0-9]"+".*"+"€"+"$";
	 		currencyFormatList.add(currencyFormat);
	 		currencyFormat ="ITA"+"_"+ "^[€]"+".";
	 		currencyFormatList.add(currencyFormat);
	 		currencyFormat ="FRA"+"_"+ "^[0-9]"+".*"+"€"+"$";
	 		currencyFormatList.add(currencyFormat);
	 		currencyFormat ="CHT"+"_"+ "^[NT$]"+".*";
	 		currencyFormatList.add(currencyFormat);
	 		//currencyFormat ="CHS"+"_"+"^[¥]"+".";
	 		//currencyFormatList.add(currencyFormat);
	 		//currencyFormat ="ESN"+"_"+"^[0-9]"+".*"+"€"+"$";
	 		//currencyFormatList.add(currencyFormat);
	 		
	 		currencyFormat ="ENU"+"_"+ "[$]\\b.*"+"\\b";
	 		currencyFormatList.add(currencyFormat);
	 
	 		return currencyFormatList;
	 }
public static LinkedHashMap<String, Set<String>> getCurrencyformat(){
	
	LinkedHashMap <String, Set<String>> hashMap = new LinkedHashMap<String,Set<String>>();
	List <String> list = getCurrencyFormatList();
	
	for (String language : list){
		
	   String[] L= language.split("_");
	   
	   Set<String> set=hashMap.get(L[0]);
	 
	   if (set==null){
		   
		   set = new LinkedHashSet<String>();
	   }
	   		set.add(L[1]);
	   
	   
	   	hashMap.put(L[0], set);
	}
	
		return hashMap;

}

public static boolean assertFormat(String label,ArrayList<String> formatLog){
	
    boolean enuExist = false;
    map = iCurrency.getCurrencyformat();
    Set<String> keys = map.keySet();
    Iterator<String> set = keys.iterator();
    
    while (set.hasNext()) {
    	
    	String l = (String)set.next();
    	
    	if (!l.equals(language)) {
    		
    			System.out.println(l+ ":" + map.get(l).toString().trim());
	 	    	
	 	    	Set<String> st = map.get(l);
	 	    	Iterator<String> it = st.iterator();
	 	    	
	 	    	while(it.hasNext()) {
	 	    		
	 	    		String currencyformat = (String) it.next();
	 	    		Pattern pDate = Pattern.compile(currencyformat);
	 				Matcher mDate = pDate.matcher(label); 
	 				if(mDate.find()){
	 					
	 					enuExist = true;
	 					String strTemp = "[iFormat]:Find an "+ l +" currency format] in [" + label.trim() + "]\r\n";
	 					System.out.println(strTemp);
	 					formatLog.add(strTemp);
	 					
	 				}
 	 	    		
	 	    	}
	 	    }
    }
    
    
    		return enuExist;

}

static boolean extractContexts(WebDriver driver,ArrayList<String> iFormatLog)
{
	int seq = 0;
	boolean ENUCurrencyformatExist = false;
    List<WebElement> elementsList = driver.findElements(By.cssSelector("body *")); 
	JavascriptExecutor js = (JavascriptExecutor) driver;
	boolean visible = false;		
	for(WebElement e: elementsList){
		try{
			
			/*check if element is visible*/
	        visible = e.isDisplayed();
	        
	        
	        /*filter out by visible and clickable*/
	        if(visible)
			{	                	
				/* detect date time format strings in value*/
	            String value = e.getAttribute("value");
	            String tagName= e.getTagName();
	            if(value != null & (tagName.equals("input") | tagName.equals("textarea") ))
				{
					value = value.trim();
					
					Pattern pDate = Pattern.compile(".*\\d+.*");
	 				Matcher mDate = pDate.matcher(value); 
	 				
	 				if (mDate.find()){
	 					boolean iformat = assertFormat(value, iFormatLog);
	 					System.out.println("value:"+value);
	                	if(iformat)
						{		
	                		ENUCurrencyformatExist = true;
	                		issueNumber++;
	                		highlightElementY(driver, e);	               
		        		}
	 					
	 				}
	                
	            }
	                		
        		js.executeScript("arguments[0].setAttribute('loc_id', arguments[1]);",e, seq);
        				
        			
				boolean hasChildren = hasChild(driver, seq);
				/*if the node has no child*/
                if(!hasChildren)
                {
					String context = e.getText().trim();
                	//System.out.println("context=" + context + "---------------");
                	
                	Pattern p = Pattern.compile(".*\\d+.*");
        			Matcher m = p.matcher(context);
                	if (m.matches()){
                		
                		System.out.println("context=" + context + "---------------");
                		boolean enuFormat = assertFormat(context, iFormatLog);
                    	if(enuFormat){
                    		ENUCurrencyformatExist = true;
                    		issueNumber++;
                    		highlightElementY(driver, e);
                    	}
                	}
                	
                }
            }
          			seq++;
		}
		catch(StaleElementReferenceException e2)
		{

		}
		catch(WebDriverException e3)
		{
			
		}
          	
	}

	 	return ENUCurrencyformatExist;
 }

}
