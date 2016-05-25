package Common;

import java.util.Vector;

import org.openqa.selenium.WebDriver;

public interface API_I18n {
	//Start all validators together
	void startAll();
	//Start validator alone
	void startHardcodeDetection();
	void startTruncationDetection();
	void startiFormatDetection();
	//Set to another WebDriver
	void setDriver(WebDriver driver);
	//Set screenshot and logs path, default is ../output/
	void setOutputPath(String path);
	//Set PLOC symbol for psuedo build. Default is [[ ]]
	void setPLOCSymbol(String symbol);
	//Return issue count after execute validators
	int getAllIssueCount();
	//Return a list of issue
	Vector<Issue> getAllIssueDetails();
	//Switch on or off the log
    void iLog(boolean logging);
}
