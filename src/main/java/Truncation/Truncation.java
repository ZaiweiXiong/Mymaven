package Truncation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import Truncation.CurrentTDetect;
import Truncation.CurrentTucationDetect;
import Truncation.ValidatorUtil;
import Common.absI18N;

public class Truncation extends absI18N{
	
	//ValidatorUtil vu =new ValidatorUtil();
	DetectElementArea dea = new DetectElementArea();
	
	@Override
	public void start(){
		System.out.println("Start truncation detection");
		this.detectTruncation(driver,"");		
	}
	
	/*public void detectTruncation(String ProjectID, int DropNumber,
			String DBPosition, String NodePath, WebDriver driver, String xpath) {
		
		if (xpath.length()==0){
			
			//xpath = "//a|//input|//label|//button|//span";
			xpath="//label[contains(text(),'')]|//a[contains(text(),'')]|//input[contains(text(),'')]|//button[contains(text(),'')]|//span[contains(text(),'')]";
			
		try {
			//startSingleCheck (driver,xpath); 
			startSingleCheck (ProjectID,DropNumber,DBPosition,NodePath,driver,xpath); 
			//startMulitiCheck(driver);
			
		}catch (Exception error) {
			error.printStackTrace();
		}
		}else {
			try {
				//startMulitiCheck(driver);
				startMulitiCheck(ProjectID,DropNumber,DBPosition,NodePath,driver);
			}catch (Exception error) {
				error.printStackTrace();
			}			
		}		
	}*/
	
	public void detectTruncation(WebDriver driver, String xpath) {		
		if (xpath.length()==0){
			
			//xpath = "//a|//input|//label|//button|//span";
			xpath="//label[contains(text(),'')]|//a[contains(text(),'')]|//input[contains(text(),'')]|//button[contains(text(),'')]|//span[contains(text(),'')]";
			
		//if(xpath!=null&&xpath.length()!=0){
						
		try {
			startSingleCheck (driver,xpath); 		
			//startMulitiCheck(driver);
			
		}catch (Exception error) {
			error.printStackTrace();
		}
		}else {
			try {
				startMulitiCheck(driver);
				//startMulitiCheck(ProjectID,DropNumber,DBPosition,NodePath,driver);
			}catch (Exception error) {
				error.printStackTrace();
			}			
		}		
	}
	
	private void startMulitiCheck(WebDriver driver)throws Exception {

		// multi threads		
		System.out.println("Start multi threads checking!");

		List<Thread> threadList = new ArrayList<Thread>();
		LinkedHashSet<String> set = new LinkedHashSet<String>();
		Pattern p = Pattern.compile(".*");
		Matcher m = p.matcher(driver.findElement(By.cssSelector("body")).getText());

		while(m.find())
		{
			if(!m.group().toString().equals(""))
			{
				String text = m.group().toString().trim();
				System.out.println("Find:" + m.group().toString().trim());
				if (!ValidatorUtil.isNumber(text)){
					set.add(text);	
				}											
			}
		}

		System.out.println("set.size: "+set.size());
				
		Iterator itr = set.iterator();
		//int tempTime = set.size();

		while (itr.hasNext()){			
			String text = itr.next().toString();
			CurrentTucationDetect ct = new CurrentTucationDetect (driver,text,set);
			Thread td = new Thread(ct);
			td.setName(text);
			td.start();
			threadList.add(td);			
		}
		for (Thread td_:threadList){
			try {
				td_.join();	
			}catch (Exception error) {
				error.printStackTrace();
			}			
		}	
		
		//dea.area_for_each(driver,set,"00000123");
		
		System.out.println("Closing browser!");
		ValidatorUtil.waitMin(3);
		//driver.close();		
	}

	/*private void startSingleCheck(String projectID, int dropNumber,
			String dBPosition, String nodePath, WebDriver driver, String xpath) throws InterruptedException {
		
		CurrentTDetect ct = new CurrentTDetect(projectID,dropNumber,dBPosition,nodePath,driver,xpath);	
		
		Thread td = new Thread(ct);
		td.setName(xpath);
		td.start();
		td.join();
		
		System.out.println("Done Truncation Check");		
	}*/
	class concurrent implements Runnable{
		public void run(){
			
		}
	
	}
	private void startSingleCheck( WebDriver driver, String xpath) throws InterruptedException {
		
		CurrentTDetect ct = new CurrentTDetect(driver,xpath);	
		
		Thread td = new Thread(ct);
		td.setName(xpath);
		td.start();
		td.join();
		
		System.out.println("Done Truncation Check");		
	}
	
}
