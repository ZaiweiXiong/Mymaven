package com.org.learningMaven;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

public class HelloWorldTest {

	@Test
	public void Login(){
		
		WebDriver driver = new FirefoxDriver ();
		
		driver.get("http://www.google.com");
		
		System.out.println("Login testing maven testng and selenium webdriver");
	} 
	@Test
	public void test() {
		System.out.println("123");
	}
}
