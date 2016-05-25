package Truncation;

import java.awt.Font;
import java.awt.FontMetrics;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

//import db.ManageDB;

public class CurrentTDetect extends Thread {
	
	final String projectID = "00000000";
	//int dropNumber;
	//String dbPosition;
	//String nodePath;
	WebDriver driver;
	private String text_;
	ValidatorUtil util = new ValidatorUtil();
	LinkedHashSet set = new LinkedHashSet();
	
	/*
	 public CurrentTDetect (String tdProjectID,int tdDropNumber,String tdDBPosition,String tdNodePath,WebDriver driver_previous,String text) {
		
		projectID = tdProjectID;
		dropNumber = tdDropNumber;
		dbPosition = tdDBPosition;
		nodePath = tdNodePath;
		driver = driver_previous;
		text_ = text;
		//set=set_;
	}
	*/
	
	public CurrentTDetect (WebDriver driver_previous,String text) {
		driver = driver_previous;
		text_ = text;
	}
		
	public void run(){
		Date date = new Date();
		long longtime = date.getTime();
		int count_h=0, count_v=0;
		
		try {							
			int count[];
			count = locate_element(driver, text_);
			count_h = count_h + count[0];
			count_v = count_v + count[1];
			//System.out.println("count.length:"+count.length);
			//System.out.println("-------------------------\n");									
		}catch (Exception error) {
		   error.printStackTrace();			
		}
		
		if (count_h+count_v>0) {			
			try {				
				String temp ="Start capture screen!";
				ValidatorUtil.setGetScreenStatus(temp);
				util.getScrnTruncation(projectID,driver);
				
				//DB
				/*ManageDB m = new ManageDB();
				m.getConnection(dbPosition);
                ResultSet rs_search=null; 
                String sql="";
                int issue = count_h+count_v;
                
                sql="insert into truncationlist set ";
				sql+="ProjectID='"+projectID+"'";
				sql+=",DropNumber="+dropNumber;
				sql+=",NodePath='"+nodePath+"'";
				sql+=",IssueOnPage='"+issue+"'";
				m.updateSQL(sql);
								 
				sql="select * from truncationlist where ";				 				 
				sql+="ProjectID='"+projectID+"'";				 					 
					
				rs_search=m.getSQL(sql); 					 					
				if(rs_search.next()){		
					System.out.println(rs_search.getRow());
					}					 									
				m.close();*/				
			}catch (Exception error){
				error.printStackTrace();							
			}								
			}else {	
				System.out.println("No trunction in this page!");
				//System.out.println("Page need to detect again!");			
				//a.area_for_each(driver,set);					
			}
				 		  		
			System.out.println("Total executing time:"+ValidatorUtil.TimeCount(longtime));						
		//driver.close();
	}
	
	int[] locate_element(WebDriver driver, String label) throws Exception
	{
		ArrayList<WebElement> objs = new ArrayList<WebElement>();				
		JComponent jl = new JLabel();
		String node = "";
		int text_width=0;
		int text_height = 0;
		int truncation_count_h = 0;
		int truncation_count_v = 0;
		int count[]={0,0};		
		String font;
						 
		//node = "//" + tag.get(i) + "[text()='"+ label + "']";				    
		//node = "//" + tag.get(i) + "//*"+"[text()='"+ label + "']";						 
		node = label;				
		//System.out.println(tag.get(i).toString()+i);				
		//System.out.println("xpath: "+node);	
		
		List<WebElement> objs_temp = elementList(driver,node);//driver.findElements(By.xpath(node));					 
		//System.out.println ("objs_temp:"+ objs_temp.size());
				 
		int tempNumber = objs_temp.size();								 
		if (tempNumber>0)				 
		{					
			for(int j=0;j<objs_temp.size();j++)					
			{						
				objs.add(objs_temp.get(j));						
				//System.out.println(j+"Text is: "+objs_temp.get(j).getText());																		
				int element_width = objs_temp.get(j).getSize().width;							
				int element_height = objs_temp.get(j).getSize().height;	
				
				int font_size = (int)Float.parseFloat(objs_temp.get(j).getCssValue("font-size").replace("px", ""));						
				//System.out.println("Dimension: " + objs_temp.get(j).getSize() + "\nFont-size:" + font_size);						
				int font_weight = Integer.parseInt(objs_temp.get(j).getCssValue("font-weight"));					    
				//System.out.println("Font weight: " + font_weight); 					    
				String font_family = objs_temp.get(j).getCssValue("font-family");					    
				//System.out.println("Font family: " + font_family); 	
					    					    
				//System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"); 						    
				//System.out.println("element_width " + "xxxxxxxxxxxxx"+element_width); 						    
				//System.out.println("element_height " + "xxxxxxxxxxxxx"+element_height); 						    
				int element_area = element_width * element_height;					    
				//System.out.println("element_area " + "xxxxxxxxxxxxx"+element_area); 	
					    					    					    
				if(font_family.indexOf(",") != -1)					    
				{					    	
					font = font_family.substring(0, font_family.indexOf(","));					    
				}					    
				else					    
				{					    	
					font = font_family;					    
				}					    
				//System.out.println("Font: " + font); 
					    					 
				if(element_width> 0)					 
				{						 
					if(font_weight == 400)						 
					{							 
						Font f = new Font(font, Font.PLAIN, font_size);							 
						FontMetrics fm = jl.getFontMetrics(f);							 
						text_width =  fm.stringWidth(label);							 
						text_height = fm.getHeight();						 							 
							 
						//System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"); 								 
						//System.out.println("Text Width: " + text_width); 							 
						//System.out.println("Text Height: " + text_height); 
							 
						int text_area = text_width * text_height;							 
						//System.out.println("Text area: " + text_area);							 
						//System.out.println("Element area: " + element_area);							  						 						 						 
					}
						 						 					
					if(font_weight == 700)						 
					{							 
						Font f = new Font(font, Font.BOLD, font_size);							 
						FontMetrics fm = jl.getFontMetrics(f);							 
						text_width = fm.stringWidth(label);							 
						text_height = fm.getHeight();
							 
						//System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
						//System.out.println("Text Width: " + text_width); 							 
						//System.out.println("Text Height: " + text_height); 							 
						int text_area = text_width * text_height;
							 
						//System.out.println("Text area: " + text_area);							 
						//System.out.println("Element area: " + element_area); 
					}
						 						
					int difference_h =element_width - text_width;						
					//System.out.println("difference_hvvvv: " + difference_h);						
					int difference_v = element_height - text_height;						
					//System.out.println("difference_vvvvvv: " + difference_v);						
					//System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"); 
					
					boolean truncation_h = false;						
					boolean truncation_v = false;
												
					if (difference_h < -160 && difference_h >-200 )							
					{							
						truncation_h = true;							
						ValidatorUtil.highlight_Element(driver, objs_temp.get(j));							
						truncation_count_h++;							
						System.out.println("difference_h:"+truncation_count_h+"::"+objs_temp.get(j).getText());												
					}
						
					if (difference_v<-6)							
					{							
						truncation_v = true;							
						truncation_count_v++;							
						ValidatorUtil.highlight_Element(driver, objs_temp.get(j));							
						System.out.println("difference_v"+truncation_count_v+"::"+objs_temp.get(j).getText());														
					}
						 
					//System.out.println("Horizonal truncation: " + truncation_h); 						 
					//System.out.println("Vertical truncation: " + truncation_v); 						 
					//System.out.println("\n");																	 					 
				}																			
			}								 
		}				
		count[0] = truncation_count_h;		
		count[1] =truncation_count_v;	
		return count;			
	}
	
	public static List<WebElement> elementList (WebDriver driver,String path) {
				
		String xpathString_ = path;
		//System.out.println("xpath:"+xpathString_);
		
		List<WebElement>  elelist = driver.findElements(By.xpath(xpathString_));
		List<WebElement> list = new ArrayList<WebElement>();
		
		int i=0;
		for (WebElement ele : elelist){			
			try{				
				if (ele.isDisplayed()&&ExpectedConditions.elementToBeClickable(By.xpath(ele.toString()))!=null){
					
					String eleString = ele.getText().toString();
					
					list.add(ele);
					i++;		
			}				
		}catch (Exception error){int k=0; k++;System.out.println("Element is no longer attached to the DOM"+k);}		
		}	
		//System.out.println("There are "+list.size()+" "+"elements");
		return  list;
	}

}
