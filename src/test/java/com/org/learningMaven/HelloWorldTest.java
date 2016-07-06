package com.org.learningMaven;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.DesiredCapabilitiesBrowser;



public class HelloWorldTest {

	@Test
	public void Login(){
		
		//WebDriver driver = new FirefoxDriver ();
		
		//WebDriver driver = DesiredCapabilitiesBrowser.webBroswer("http://155.35.154.157:4444/wd/hub", "en", 1200,1200);
		//driver.get("http://www.google.com");
		try {
			Thread.sleep(3000);
		}catch (Exception error) {
			error.printStackTrace();
		}
		
		
		//driver.close();
		System.out.println("Login testing maven testng and selenium webdriver");
	} 
	@Test
	public void test() {
		System.out.println("123");
	}
}
