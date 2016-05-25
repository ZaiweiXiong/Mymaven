package Utility;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import javax.imageio.ImageIO;
import mapping.mappingUpdates;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class CaptureDropDownScreen {
	
	   private static int screen_id =0;
	   static WebDriver driver;
	   static Point text_location;
	   static BufferedImage img ;
	   static List<String> xpaths = new ArrayList<String>();
	   static List<WebElement> els =new ArrayList<WebElement>();
	   static List<WebElement> list = new ArrayList<WebElement>();		//elementlist;
	
	public static void captureDropdownMenu (WebDriver driver, String projectID) throws Exception{}
	
	public static void captureDropdownMenu (WebDriver driver) throws Exception {
		
		List<WebElement> eles = elementListSelect(driver,"//select");
		List <String> xpaths_ = new ArrayList<String>();
		
		int j=0;
		if (eles.size()!=0) {
			
			 for (WebElement element_:eles){xpaths_=returnXpaths (driver,element_);}
			 
			 Object [] strs = xpaths_.toArray(); 
			
			 
			 for (Object s:strs){ 
				 
				 if (xpaths_.indexOf(s)!=xpaths_.lastIndexOf(s)){
					 xpaths_.remove(s);
				 }
			 }
			 //System.out.println("after remove the list size:"+xpaths_.size());
			 
			 int k=0;
			 while(k<xpaths_.size()){
				  
				  String selectXpath=xpaths_.get(k);
				  BufferedImage img;
				  BufferedImage img1;
				 
				  WebElement ele = driver.findElement(By.xpath(xpaths_.get(k)));
				  ele.click();
				  
				  img1 = TextFormat.captureScrn(driver);//TextFormat.getScrn(driver);/////
				  String path = "./screen//";
				  String name =String.valueOf(screen_id);
				  
				  
				  //System.out.println("selectXpath:value:xxxxxxxxxxxxxxx"+selectXpath);
				   
				  if (ele.getTagName().equals("select")){  
					 
					  //first judge
					  
					  img = TextFormat.gsscAddTooltip(driver ,ele, img1, selectXpath);   //add the drop down menu to img1
					  
					  SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss"); //create new image with drop down menu
					  format.setTimeZone(TimeZone.getTimeZone("GMT"));
					  Date date = new Date();
					  String strDate = format.format(date);
					  String PID = "00000";
					  String screenName = PID + "dropDownMenu" + strDate + "_" + screen_id + ".png";
					  String screenName_full =  screenName;
					  screen_id++;
					  ImageIO.write((RenderedImage) img, "png", new File(path+screenName_full));
					  /////////////////////////
					  //delete original screens
					  File file = new File("./screen//"+TextFormat.ScreenName); 
					  file.deleteOnExit();
					  ////////////////////////
					  //start mapping with options from select drop menu
					  String tagString = ele.getTagName();
					  String selectOptions ="/option[%s]";
					  String String_li="//li[%s]";
					  
					  if (tagString.equals("select")) {
						  
						  Select select  = new Select(ele.findElement(By.xpath(selectXpath))) ;
						  List <WebElement> options  = select.getOptions();
						  int option_id=0;
						  for(option_id=0;option_id<options.size();option_id++){
							  
							  String selectOption_=String.format(selectXpath+selectOptions,option_id+1);
							  //System.out.println("selectOption_:"+selectOption_);
							  //System.out.println("option_id:"+option_id);
							  if (!driver.findElement(By.xpath(selectOption_)).getText().equals("")){
								  
								  	mapping.mapping.start(driver, screenName_full, selectOption_);
							  }
							 
						  }
						  
					  }else {}
					  
				  }else {}
				  	 	//ImageIO.write((RenderedImage) img, "png", new File(path+"12990_"+name+".png"));//test codes
				  		k++;
				}
			
			
				}else {System.out.println("There are not drop down mean");}
		
	}
	public static void captureTooltip (WebDriver driver) {
			
			try {
				
			List<WebElement> els = avaiableElement_tooltips(driver);
			
			int k=0;
			BufferedImage img1;
			img1 = TextFormat.captureScrn(driver);
			//TextFormat.getScrn(driver);
			//TextFormat.captureScrn(driver);  //original screen
			BufferedImage img =null;
				
////////////////////////////////////////////////////////////////////////////////////		
			while (k<els.size()) {
				
				  String path = "./screen//";
				  String name =String.valueOf(screen_id);
				  img = TextFormat.gsscAddTooltip(driver ,els.get(k), img1); 
				  ///////////////////////////////////////////////////////////////////////
				  
				  SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss"); 		//create new image with tooltips
				  format.setTimeZone(TimeZone.getTimeZone("GMT"));
				  Date date = new Date();
				  String strDate = format.format(date);
				  String PID = "00000";
				  String screenName = PID + "Tooltips" + strDate + "_" + screen_id + ".png";
				  String screenName_full =  screenName;
				  screen_id++;
				  ImageIO.write((RenderedImage) img, "png", new File(path+screenName_full));
				  
				  //delete img1;
				  /* 
				  File file = new File("./screen//"+TextFormat.ScreenName);
				  file.deleteOnExit();
				  System.out.println("delete:"+TextFormat.ScreenName);
				  */
				  
				  //start mapping
				  try {
					  if (!els.get(k).getAttribute("data-qtip").toString().equals("")){
						  
						  String s =  returnxpath(driver,els.get(k));  						//els.get(k).toString();//"//em/button";
						  String str_ = els.get(k).getAttribute("data-qtip").toString();
						  String str = s+"[@data-qtip='%s']";						    	////em/button[@data-qtip='±í Name Search ˆö[[BABE1]]']
						  String xpath_=String.format(str, str_);
						  
						  mappingUpdates.startMapping_tooltips(driver, screenName_full, xpath_);
						  //mappingUpdates.startMapping_tooltipsIE(driver, screenName_full, xpath_);
						 
					  }
				  }catch (Exception error){
					  
					  if (!els.get(k).getAttribute("title").toString().equals("")) {
						  
						  String s =  returnxpath(driver,els.get(k));  						//els.get(k).toString();//"//em/button";
						  String str_ = els.get(k).getAttribute("title").toString();
						  String str = s+"[@title='%s']";						    		////em/button[@data-qtip='±í Name Search ˆö[[BABE1]]']
						  String xpath_=String.format(str, str_);
						  
						  mappingUpdates.startMapping_tooltips(driver, screenName_full, xpath_);
						  //mappingUpdates.startMapping_tooltipsIE(driver, screenName_full, xpath_);
						  //System.out.println("xpath:"+xpath_);  
					  }
					  
				  } 
				  
				  	k++;
			}
			  		deleteFile("Tooltips_original.png");
			 
			  
			}catch (Exception error) {error.printStackTrace();}
}
	
	public static List<WebElement> elementList (WebDriver driver,String path) {
		
		String xpathString_ = path;
		List<WebElement>  elelist = driver.findElements(By.xpath(xpathString_));
		
		
		int i=0;
		if (elelist.size()!=0) {
		for (WebElement ele : elelist){
			
			try{
				
				if (ele.isDisplayed()&&ExpectedConditions.elementToBeClickable(By.xpath(ele.toString()))!=null){
					if (!ele.getText().equals("") || ele.getText().contains("[[")){  		//ele.getText()!="" && ele.getText().contains("[[")
					list.add(ele);
					//System.out.println("Text:"+ele.getText());
					i++;
					}else if (ele.getAttribute("value").toString().contains("[[")||!ele.getAttribute("value").toString().equals("")){
						//System.out.println("value:"+ele.getAttribute("value").toString());
						list.add(ele);
						i++;
					}else if(ele.getAttribute("placeholder").toString().contains("[[")){
						//System.out.println("placeholder:"+ele.getAttribute("placeholder").toString());
						list.add(ele);
						i++;
					}
				}
			
				
			}catch (Exception error){
				//int k=0; k++;System.out.println("Element is no longer attached to the DOM"+k);
			}
		}
	}
		
		//System.out.println("There are "+list.size()+" "+"elements");
		return  list;
	}
	public static List<WebElement> elementListSelect (WebDriver driver,String path){
		
		String xpathString_ = path;
		List<WebElement>  elelist = driver.findElements(By.xpath(xpathString_));
		List<WebElement> list = new ArrayList<WebElement>();
		
		int i=0;
		if (elelist.size()!=0) {
		for (WebElement ele : elelist){
			
			try{
				
				if (ele.isDisplayed()&&ExpectedConditions.elementToBeClickable(By.xpath(ele.toString()))!=null){
					list.add(ele);
					}
			
				
			}catch (Exception error){int k=0; k++;System.out.println("Element is no longer attached to the DOM"+k);}
		}
	}
		
		System.out.println("There are "+list.size()+" "+"Select elements");
		return  list;
}
	
	public static Object[] removeDupList (List<String> strs) throws Exception {
		
		Object[] objs  = strs.toArray();
		
		for (Object s : objs){
			
			if (strs.indexOf(s)!=strs.lastIndexOf(s)){
				strs.remove(s);
			}
			
		}
		
			return objs;
	}

	private static List<String> returnXpaths (WebDriver driver,WebElement element) throws Exception {
		
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
			 	System.out.println("xpath value:"+xpath);
				List<WebElement> eles  = elementListSelect(driver,xpath);		//element.findElements(By.xpath(xpath));
				System.out.println("eles.size:"+eles.size());
				String path = "//select[@local_id='%s']";
				if (eles.size()>1){
					
					//System.out.println("id:"+element.getAttribute("id"));
					for (int i=0;i<eles.size();i++){
						TextFormat.SetLocalId(driver,eles.get(i),i);
						String str = String.format(path, i);
						System.out.println(":"+str);
						Thread.sleep(3000);
						//driver.findElement(By.xpath(str)).click();
						xpaths.add(str);
					}
				
					
				}else{
					System.out.println("eles.size else:"+eles.size());
					xpaths.add(xpath);
				}
		  		
		  		
					return xpaths;
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
	
public static List<WebElement> avaiableElement_tooltips(WebDriver driver) {
	
		System.out.println("Starting check tooltips from GUI...");
		
		List<String> Xpathtooltips =new ArrayList<String>();
		List<WebElement> listEles_tooptips = new ArrayList<WebElement>();
		
		Xpathtooltips.add("//body//button");
		Xpathtooltips.add("//tr//a");
		Xpathtooltips.add("//a//span");
		Xpathtooltips.add("//input");
		Xpathtooltips.add("//td");
		
		
		
		for (int k=0;k<Xpathtooltips.size();k++) {
			
			els = elementList(driver, Xpathtooltips.get(k).toString());	 	//body//button ////em/button
		}
		////////////////////
		Map<String,WebElement> map= new LinkedHashMap<String,WebElement>();
		
		for (int i=0;i<els.size();i++){
			
			try {
				
				if (!els.get(i).getAttribute("data-qtip").toString().equals("")){
					map.put(els.get(i).getAttribute("data-qtip").toString(), els.get(i));
				}
				
			}catch(Exception error){
				if (!els.get(i).getAttribute("title").toString().equals("")){
					map.put(els.get(i).getAttribute("title").toString(), els.get(i));
				}//else if () {}
				
				else {
					{
						//System.out.println("no dat-qtip or title");
					}
				}
			}
		}
		
		Set set = map.keySet();
		Iterator it = set.iterator();
		while(it.hasNext()){
			
			String keys = (String)it.next();
			WebElement ele= map.get(keys);
			listEles_tooptips.add(ele);
			
		}
		
		
		///////////////////
		
		System.out.println("There are "+listEles_tooptips.size()+" "+"tooltips");
		
		
			return listEles_tooptips;
	}
 public static void deleteFile (String fileName){
	 
	  try {
		  
		  File file = new File("./screen/"+fileName);
		  Thread.sleep(3000);
		  //file.deleteOnExit();
		  file.delete();
		  //System.out.println("delete-file.delete:"+fileName);
		  
		  
	  }catch (Exception error) {
		  System.out.println("error");
	  }
	  
	  
 	} 
}
