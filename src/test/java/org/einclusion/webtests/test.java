package org.einclusion.webtests;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy.ProxyType;


public class test {
	public static void main(String []args)
	{
		WebDriver dr = null;
		org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy(); 
		proxy.setSslProxy("proxyurl"+":"+8080); 
		proxy.setFtpProxy("proxy url"+":"+8080); 
		proxy.setSocksUsername("SSSLL277"); 
		proxy.setSocksPassword("password"); 

		DesiredCapabilities dc = DesiredCapabilities.firefox();
		dc.setCapability(CapabilityType.PROXY, proxy); 
		dr = new FirefoxDriver(dc);
		
		
		//WebDriver driver = new FirefoxDriver();
		//driver.get("http://localhost:8080/");
		//dr.get("http://www.google.lv/?gws_rd=ssl");
		dr.get("http://localhost:8080");
		//dr.findElement(By.xpath("//a[text()='Noteikumi']")).click();
		//WebElement buttonToClick = dr.findElement(By.className("hidden-tablet"));
		//((JavascriptExecutor)dr).executeScript("arguments[3].click();",buttonToClick );
//		dr.close();
		
	}
}
