package Common;

import java.util.Iterator;
import java.util.Vector;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import HardCode.hardcode;
import Truncation.Truncation;
import iNumber.iNumber;
import iFormat.iFormat;
import iCurrency.iCurrency;

public class I18NTesting implements API_I18n{
	
	absI18N hardcode;
	absI18N truncation;
	absI18N iFormat;
	absI18N iNumber;
	absI18N iCurrency;
	
	WebDriver driver;
	/*
	 * initiate issue list
	 */
	Vector<Issue> allIssues = new Vector<Issue>();
	/*
	 * default output path
	 */
	String outputPath = "./output/";
	/*
	 * default symbol
	 */
	String PLOCSymbol = "[[";
	/*
	 * Constructor - local web driver
	 */
	
	
	public I18NTesting(WebDriver driver){
	
		System.out.println("Use Local WebDriver");
		this.hardcode = new hardcode();		
		this.truncation = new Truncation();
		this.iFormat = new iFormat();
		this.driver = driver;
		this.iLog(true);
		
	}
	
	public I18NTesting(RemoteWebDriver driver,String language){
		
		System.out.println("Use Remote WebDriver");
		this.hardcode = new hardcode();
		//this.truncation = new Truncation();
		//this.iFormat = new iFormat();
		this.iNumber =new iNumber(language);
		this.iCurrency = new iCurrency(language);
		this.driver = driver;
		this.iLog(true);
	}
	/*
	 * (non-Javadoc)
	 * @see Interface.I18n#startAll()
	 */
	public void startAll(){
		this.startHardcodeDetection();
		this.startiFormatDetection();
		this.startTruncationDetection();
	}
	/*
	 * (non-Javadoc)
	 * @see Interface.I18n#startHardcodeDetection()
	 */
	public void startHardcodeDetection(){
		this.hardcode.setOuputPath(this.outputPath + "hardcode");
		this.hardcode.setDriver(this.driver);
		this.hardcode.setPLOCSymbol(this.PLOCSymbol);
		this.hardcode.start();
	}
	/*
	 * (non-Javadoc)
	 * @see Interface.I18n#startTruncationDetection()
	 */
	public void startTruncationDetection(){
		
		this.truncation.setDriver(this.driver);
		this.truncation.setOuputPath(this.outputPath + "truncation");		 
		this.truncation.start();
	}
	/*
	 * (non-Javadoc)
	 * @see Interface.I18n#startiFormatDetection()
	 */
	public void startiFormatDetection(){
		this.iFormat.setDriver(this.driver);
		this.iFormat.setOuputPath(this.outputPath + "iFormat");
		this.iFormat.start();
	}
	
	public void startiNumberDetection(){
		this.iNumber.setDriver(this.driver);
		this.iNumber.setOuputPath(this.outputPath + "iNumber");
		this.iNumber.start();
	}
	public void startiNumberDetection(String language){
		this.iNumber.setDriver(this.driver);
		this.iNumber.setOuputPath(this.outputPath + "iNumber");
		this.iNumber.start();
	}
	public void startiCurrencyDetection(){
		this.iCurrency.setDriver(this.driver);
		this.iCurrency.setOuputPath(this.outputPath + "iCurrency");
		this.iCurrency.start();
	}
	
	/*
	 * (non-Javadoc)
	 * @see Interface.I18n#setDriver(org.openqa.selenium.WebDriver)
	 */
	public void setDriver(WebDriver driver){
		this.driver = driver;
	}
	/*
	 * (non-Javadoc)
	 * @see Interface.I18n#setOutputPath(java.lang.String)
	 */
	public void setOutputPath(String path){
		this.outputPath = path;
	}
	/*
	 * (non-Javadoc)
	 * @see Interface.I18n#setPLOCSymbol(java.lang.String)
	 */
	public void setPLOCSymbol(String symbol){
		this.PLOCSymbol = symbol;
	}
	/*
	 * (non-Javadoc)
	 * @see Interface.I18n#getAllIssueCount()
	 */
	public int getAllIssueCount(){
		return this.hardcode.getIssueCount() + this.iFormat.getIssueCount() + this.truncation.getIssueCount();
	}
	/*
	 * (non-Javadoc)
	 * @see Interface.I18n#getAllIssueDetails()
	 */
	public Vector<Issue> getAllIssueDetails(){
		this.addIssuesOneByOne(this.hardcode.getIssueDetails());
		this.addIssuesOneByOne(this.iFormat.getIssueDetails());
		this.addIssuesOneByOne(this.truncation.getIssueDetails());		
		return allIssues;
	}
	/*
	 * a private method to copy all issue details to one issue list
	 * @Parameter allIssues
	 */
	private final void addIssuesOneByOne(Vector<Issue> issues){
		Iterator<Issue> it = issues.iterator();
		while(it.hasNext()){
			this.allIssues.add((Issue) it.next());
		}
	}
	
	public void iLog(boolean logging){
		
	}
}
