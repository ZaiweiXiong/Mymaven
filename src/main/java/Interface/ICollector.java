package Interface;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public interface ICollector {
	
	public final List<WebElement> currentElementList =new ArrayList<WebElement>() ;
	
	public WebDriver setDriver();
	List<WebElement> searchAvailableElements();
	List<String> elementsByType () ;
	
	public List<WebElement> getInputs();
	public void processInput (List<WebElement> inputs ); 
	
	public List<WebElement> getSelects();
	public void processSelect (List<WebElement> select ); 
	
	public List<WebElement> getButtons();
	public void processButton (List<WebElement> buttons ); 
	
	public List<WebElement> getRadioOption();
	public void processRadioOption (List<WebElement> radios ); 
	
	public List<WebElement> getCheckBox();
	public void processCheckBox (List<WebElement> checkboxes );
	
}
