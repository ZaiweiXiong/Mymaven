package Common;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;

public abstract class absI18N {
	
	private Vector<Issue> issues = new Vector<Issue>();
	String iss = "";
	public WebDriver driver;
	public String outputPath;
	public String symbol;
	private static int screenID = 0;
	 		boolean remote = false;
	public IssueCategory Category = new IssueCategory();
	public void start(){
		
	}
	public void addNewIssue(Issue e){
		this.issues.add(e);
	}
	public int getIssueCount(){
		
		return issues.size();
	}
	public Vector<Issue> getIssueDetails(){
		
		return issues;
	}
	public void setDriver(WebDriver driver){
		this.driver = driver;
	}
	public void setOuputPath(String path){
		this.outputPath = path;
	}
	public void setPLOCSymbol(String symbol){
		this.symbol = symbol;
	}
	public void useRemoteWebDriver(){
		this.remote = true;// this method cannot be invoked by abstract instance
		this.driver = new Augmenter().augment(this.driver);
	}
	public String CaptureScreenShot(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss"); 
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date date = new Date();
		String strDate = format.format(date);

		String screenName = strDate + "_" + absI18N.screenID + ".png";
		String screenName_full = this.outputPath + "/" + screenName;
		absI18N.screenID++;
		try
		{
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(screenName_full));
			System.out.println("Screenshot: " + screenName_full);
		}
		catch(WebDriverException e)
		{
			System.out.println("//----------Screen Capture failed! Wait 5 seconds and Capture again!");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e2) {
			
				e2.printStackTrace();
			}
			File scrFile = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
			System.out.println("Screenshot: " + screenName_full);

			try {
				FileUtils.copyFile(scrFile, new File(screenName_full));
				
			} catch (IOException e1) {

				e1.printStackTrace();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
				return screenName_full;
	}
	public void Highlight(){
		
	}
	public static void highlightElementY(WebDriver driver, WebElement element) {
        String eStyle = element.getAttribute("Style");
        //for (int i = 0; i < 5; i++) {
               String forecolor = "color: red;";       
               String backcolor = "background-color: yellow;";
               String outline = "outline: 1px solid rgb(126,255,136);";
               String color;
              
                     color = eStyle + forecolor + backcolor + outline;
                     // System.out.println("color: " + color);
              
                 

//             String color_org = element.getAttribute("style").toString();
               JavascriptExecutor js = (JavascriptExecutor) driver;
               js.executeScript(
                            "arguments[0].setAttribute('style', arguments[1]);",
                            element, color);

//             js.executeScript(
//                          "arguments[0].setAttribute('style', arguments[1]);",
//                          element, color_org);

        //}
 }
}
