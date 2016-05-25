package validatorTruncation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;


public class ValidatorUtil {

	public static int screen_id = 0;
	
	public static void highlight_Element(WebDriver driver, WebElement element) {
		for (int i = 0; i < 5; i++) {
			String changecolor = "background-color: red;";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(
					"arguments[0].setAttribute('style', arguments[1]);",
					element, changecolor);


		}
	}
	public static ArrayList<String> createTaglist()
	{
		ArrayList<String> tag_list= new ArrayList<String>();
		tag_list.add("button");
		tag_list.add("span");
		tag_list.add("div");
		tag_list.add("label");
		tag_list.add("td");
		tag_list.add("tr");
		tag_list.add("ul");
		tag_list.add("a");
		tag_list.add("h1");
		tag_list.add("h2");
		tag_list.add("b");
		return tag_list;
	}
	public static String compose(int level)
	{
		String node="']";;
		for(int i=0;i<level;i++)
		{
			node = node + "/..";
		}
		node = node + "//following-sibling::*[1]//input";
		return node;
	}
	public static void waitMin(int second)
	{
		System.out.println("Wait " + second + " seconds!");
		try {
			int i;
			for(i=second;i>0;i--)
			{
			System.out.println(i);
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void getScrn(WebDriver driver)
			
	{
		try
		{
		File scrFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		String screenName = "./screen/" + String.valueOf(screen_id) + ".png"; 
		try
		{
		FileUtils.copyFile(scrFile, new File(screenName));
		}
		catch(IOException e)
		{
			System.out.println(e.toString());
		}
		screen_id++;
		}
		catch(WebDriverException e)
		{
			System.out.println("//----------Screen Capture failed! Capture again!");
			waitMin(5);
			String screenName = "./screen/" + String.valueOf(screen_id) + ".png"; 
			File scrFile = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
			try
			{
			FileUtils.copyFile(scrFile, new File(screenName));
			}
			catch(IOException e2)
			{
				System.out.println(e2.toString());
			}
			screen_id++;
			
		}

	}
	
	public static void adjustWindow(WebDriver driver)
	{
		Dimension window = driver.manage().window().getSize();
				    int height = window.height;
				    height = height/2;
				    Dimension win = new Dimension(window.width,height);
				    //driver.manage().window().setSize(win);
				    driver.manage().window().maximize();
	}
	
	
public static void highlightElement(WebDriver driver, WebElement element) 
	
	{          
		JavascriptExecutor js = (JavascriptExecutor) driver;       
		 js.executeScript("element = arguments[0];" +  "original_style = element.getAttribute('style');" +                
			"element.setAttribute('style', original_style + \";" + "background: yellow; border: 2px solid red;\");" +            
			"setTimeout(function(){element.setAttribute('style', original_style);}, 1000);", element); 
	}

}
