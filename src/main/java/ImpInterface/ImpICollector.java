package ImpInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;


import org.openqa.selenium.support.ui.Select;

import Interface.ICollector;

public class ImpICollector implements ICollector {
	
	public WebDriver driver;
	List<WebElement>  elelist = new ArrayList<WebElement>();
	
	public  ImpICollector (WebDriver driver) {
		this .driver=driver;
	}
	
	
	public WebDriver setDriver() {
		
		    driver.manage().window().maximize();
		
     		return driver;
	}

	@Override
	public List<WebElement> searchAvailableElements() {
		
		List<String> paths  = elementsByType();
		List<WebElement> list = new ArrayList<WebElement>();
		
		try{
			Thread.sleep(3000);
		}catch (Exception error) {
			
		}
		
		for (int i=0;i<paths.size();i++ ){ 
			
	    String xpathString_ = paths.get(i).toString();
	    //System.out.println(xpathString_);
	    elelist = driver.findElements(By.xpath(xpathString_));
	    //System.out.println(elelist.size());
	    
		for (WebElement ele : elelist){
			
			try{
				System.out.println("type:"+ele.getAttribute("type").toString()+"TagText:"+ele.getText().toString());
				if (ele.isDisplayed()&&ExpectedConditions.elementToBeClickable(By.xpath(ele.toString()))!=null){
					//ele.getAttribute("type");
					//System.out.println("type:"+ele.getAttribute("type").toString());
					list.add(ele);
					}
				
		  }catch (Exception error){int k=0; k++;System.out.println("Element is no longer attached to the DOM"+k);}
		}
	}
		
		System.out.println("There are "+list.size()+" "+"elements");
		return  list;
	}

	@Override
	public List<String> elementsByType() {
		
		List<String> paths = new ArrayList<String>();
		paths.add("//input");
		paths.add("//button");
		
		return paths;
	}

	@Override
	public List<WebElement> getInputs() {
		
		elelist = driver.findElements(By.xpath("//input"));
		List<WebElement> list = new ArrayList<WebElement>();
		
		if (elelist.size()!=0){
		for (WebElement ele : elelist){
			
			try{
				if (ele.isDisplayed()&&ExpectedConditions.elementToBeClickable(By.xpath(ele.toString()))!=null){
					if (!ele.getAttribute("placeholder").toString().contains("Select")) {
						
						if (!ele.getAttribute("type").equals("radio")) {
							
							if (!ele.getAttribute("type").equals("checkbox")) {
							
								if (ele.getAttribute("type").equals("text")|ele.getAttribute("type").equals("password")) {
									list.add(ele);
									ICollector.currentElementList.add(ele);
								
							}
						}
					}
				}
			}
				
		  }catch (Exception error){int k=0; k++;System.out.println("Element is no longer attached to the DOM"+k);}
			}
		}
		   
			processInput(list);
			return list;
	}

	@Override
	public void processInput(List<WebElement> inputs ) {
		
		RandomString randomString = new RandomString(6);
		
		for (int i=0;i<inputs.size();i++) {
			
			if (inputs.get(i).getAttribute("id").contains("baseurl")) {
				inputs.get(i).sendKeys("http:www."+randomString.nextString()+".com");
			}else {
				inputs.get(i).sendKeys("AutoInput"+randomString.nextString());
			}
			
		}
		
	}
	
	@Override
	public List<WebElement> getButtons() {

		elelist = driver.findElements(By.xpath("//button"));
		List<WebElement> list = new ArrayList<WebElement>();
		for (WebElement ele : elelist){
			
			try{
				
				if (ele.isDisplayed()&&ExpectedConditions.elementToBeClickable(By.xpath(ele.toString()))!=null){
					
						list.add(ele);
					
					}
				
		  }catch (Exception error){int k=0; k++;System.out.println("Element is no longer attached to the DOM"+k);}
		}
			processButton(list);
			return list;
	
	}

	@Override
	public void processButton(List<WebElement> buttons) {
		
	
		for (int i=0;i<buttons.size();i++) {
			
			
			highlightElement(driver, buttons.get(i)) ;
			
		}
			
			System.out.println("button:"+buttons.size());
	
	}

	@Override
	public List<WebElement> getSelects() {
		
		elelist = driver.findElements(By.xpath("//select"));
		List<WebElement> list = new ArrayList<WebElement>();
		
		if (elelist.size()!=0){
			
		for (WebElement ele : elelist){
			
			try{
				
				if (ele.isDisplayed()&&ExpectedConditions.elementToBeClickable(By.xpath(ele.toString()))!=null){
					
					 {
						list.add(ele);
						//highlightElement(driver,ele) ;
					}
					
				}
				
		  }catch (Exception error){int k=0; k++;System.out.println("Element is no longer attached to the DOM"+k);}
		}
	}	
		//System.out.println("before process select:"+elelist.size());
		processSelect(list);
	
		return list;
	}

	@Override
	public void processSelect(List<WebElement> select ) {
	
		for (int i=0;i<select.size();i++) 
		{
			
		try {
			
			String selectId = select.get(i).getAttribute("id");
			//String s = ImpICollector.returnxpath(driver,select.get(0));
		
			String selectXpath="//select[@id="+"'"+selectId+"'"+"]";
			
			String selectOptions_="/option[%s]";
			
			Select sel = new Select(driver.findElement(By.xpath(selectXpath)));
			List<WebElement> options_ = sel.getOptions();
			//for (int k=0;k<options_.size();k++) {
				
				try {
					int k=0;
					if (options_.size()<6){ 
						
						String xpath__= String.format(selectXpath+selectOptions_,k+1);
						String text = driver.findElement(By.xpath(xpath__)).getText();
						
						if (!text.equals("")) {
							//System.out.println(text);
							sel.selectByIndex(k);
							if(getRecentInputs(ICollector.currentElementList).size()>0){
								processInput(getRecentInputs(ICollector.currentElementList));
							}
					   
						}else {
							sel.selectByIndex(k+1);	
							if(getRecentInputs(ICollector.currentElementList).size()>0){
								processInput(getRecentInputs(ICollector.currentElementList));
							}
						}
						
					}else if ((options_.size()>6)) {
						String xpath__= String.format(selectXpath+selectOptions_,k+2);
						String text = driver.findElement(By.xpath(xpath__)).getText();
						sel.selectByIndex(k+2);	
						if(getRecentInputs(ICollector.currentElementList).size()>0){
							processInput(getRecentInputs(ICollector.currentElementList));
						}
					}
					
				}catch (Exception error){
					//System.out.println("text = null!");
				}
				
				
			//}
			
			//System.out.println("selectXpath:"+selectXpath);
			
			
		}catch(Exception error){System.out.println("not found select element");}
	}
	   
		}
	public List<WebElement> getRecentInputs(List<WebElement> inputs) throws Exception{
		
		System.out.println("before remvoe strs.size -inputs "+inputs.size());
		
		List<WebElement> els = driver.findElements(By.xpath("//input"));
		
		List<String> strs = new ArrayList<String>();
		List<String> ids = new ArrayList<String>();
		
		
		for (int i=0;i<inputs.size();i++){
			
			strs.add(inputs.get(i).getAttribute("id").toString());
			System.out.println(inputs.get(i).getAttribute("id").toString());
		}
		
		for (WebElement ele : els ){
			
			try{
				
				if (ele.isDisplayed()&&ExpectedConditions.elementToBeClickable(By.xpath(ele.toString()))!=null){
					if (!ele.getAttribute("placeholder").toString().contains("Select")) {
						
						if (!ele.getAttribute("type").equals("radio")) {
							
							if (!ele.getAttribute("type").equals("checkbox")) {
							
								if (ele.getAttribute("type").equals("text")|ele.getAttribute("type").equals("password")) {
									ids.add(ele.getAttribute("id").toString());
									System.out.println("ele: "+ele.getAttribute("id").toString());
								
							}
						}
					}
				}
			}
				
		  }catch (Exception error){int k=0; k++;System.out.println("Element is no longer attached to the DOM"+k);}
	}
		List<String> ids_ = findDifferentId(ids,strs);
		List<WebElement> RecentInputs = new ArrayList<WebElement>();
		
		for (int i=0;i<ids_.size();i++){
			
			RecentInputs.add(driver.findElement(By.id(ids_.get(i))));
		}
		
			return RecentInputs;
	}
private static String returnxpath (WebDriver driver,WebElement element) throws Exception {
		
		String temp = element.toString();
		String xpath="";
		
		if (temp.endsWith("]")){
			
			String [] strs = temp.replace(":", ";").split(";");
			
				if (strs[strs.length-1].endsWith("]")){
					int pos = strs[strs.length-1].lastIndexOf("]");
					strs[strs.length-1].substring(0,pos).trim();
				    xpath = strs[strs.length-1].substring(0,pos).trim();
				}
			
			}	
					return xpath;
	}
	
	public static void highlightElement(WebDriver driver, WebElement element) 

	{          
		JavascriptExecutor js = (JavascriptExecutor) driver;       
		 js.executeScript("element = arguments[0];" +  "original_style = element.getAttribute('style');" +                
			"element.setAttribute('style', original_style + \";" + "background: yellow; border: 2px solid red;\");" +            
			"setTimeout(function(){element.setAttribute('style', original_style);}, 6000);", element); 
	}
	
	public static void number_Element(WebDriver driver, WebElement element,int seq_number) {
			
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('loc_id', arguments[1]);",element, seq_number);
	}
	public static Object[] removeDupList (List<String> strs) throws Exception {
		
		Object[] objs  = strs.toArray();
		
		for (Object s : objs){
			
			if (strs.indexOf(s)!=strs.lastIndexOf(s)){
				strs.remove(s.toString());
			}
			
		}
		
			return objs;
	}
	public static List<String> findDifferentId(List<String> listOne, List<String> listTwo)throws Exception {
		
		Collection<String> similar = new HashSet<String>(listOne);
        Collection<String> different = new HashSet<String>();
        List<String> differentList =new ArrayList<String>();
        
        different.addAll( listOne);
        different.addAll( listTwo );
        
        similar.retainAll(listTwo);
        different.removeAll(similar);
        Iterator<String> it = different.iterator();
        
        while(it.hasNext()){
        	differentList.add(it.next().toString());
        	}
        return  differentList;
	}

	@Override
	public List<WebElement> getRadioOption() {
		
		elelist = driver.findElements(By.xpath("//input[@type='radio']"));
		List<WebElement> list = new ArrayList<WebElement>();
		for (WebElement ele : elelist){		
			try{				
				if (ele.isDisplayed()&&ExpectedConditions.elementToBeClickable(By.xpath(ele.toString()))!=null){
						list.add(ele);
					}					
				}			
			catch (Exception error){		  
				int k=0; k++;			  
				System.out.println("Element is no longer attached to the DOM"+k);
			}		
		}			
		processRadioOption(list);			
		return list;
	}

	@Override
	public void processRadioOption(List<WebElement> radios) {
		
		for (int i=0;i<radios.size();i++) {						
			if(radios.get(i).isSelected()){
				continue;
			}
			radios.get(i).click();
		}			
			
			System.out.println("Radio button:"+radios.size());			
	}

	@Override
	public List<WebElement> getCheckBox() {
		
		elelist = driver.findElements(By.xpath("//input[@type='checkbox']"));
		
		List<WebElement> list = new ArrayList<WebElement>();
		
		for (WebElement ele : elelist){					
			try{					
				if (ele.isDisplayed()&&ExpectedConditions.elementToBeClickable(By.xpath(ele.toString()))!=null){					
						list.add(ele);
					}					
				}			
			catch (Exception error){				
				int k=0; k++;					
				System.out.println("Element is no longer attached to the DOM"+k);				
			}		
		}			
			processCheckBox(list);			
			return list;
	}

	@Override
	public void processCheckBox(List<WebElement> checkboxes) {
		
				for (int i=0;i<checkboxes.size();i++) {					
					if(checkboxes.get(i).isSelected()){				
						checkboxes.get(i).clear();			
					}		
					checkboxes.get(i).click();
				}							
				System.out.println("Checkboxes:"+checkboxes.size());		
		
	}
	
}
