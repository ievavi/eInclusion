package org.einclusion.webtests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class SeleniumTest {

	@Test
	public void test() {
		try {

			WebDriver dr = null;
//			org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
//			proxy.setSslProxy("proxyurl" + ":" + 8080);
//			proxy.setFtpProxy("proxy url" + ":" + 8080);
//			proxy.setSocksUsername("SSSLL277");
//			proxy.setSocksPassword("password");

//			DesiredCapabilities dc = DesiredCapabilities.firefox();
//			dc.setCapability(CapabilityType.PROXY, proxy);

			dr = new FirefoxDriver();

			// WebDriver driver = new FirefoxDriver();
			// driver.get("http://localhost:8080/");
			// dr.get("http://www.google.lv/?gws_rd=ssl");
			dr.get("http://localhost:8080/");
			dr.findElement(By.cssSelector("a[href='M1/prediction.jsp']")).click();
			dr.findElement(By.xpath("//table/tbody/tr[1]/td[5]")).click();
			String topic = dr
					.findElement(By.xpath("//html/body/div[2]/div[1]/div/div/div/div[2]/table/tbody/tr[1]/td[2]"))
					.getText();
			String name = dr
					.findElement(By.xpath("//html/body/div[2]/div[1]/div/div/div/div[2]/table/tbody/tr[1]/td[3]"))
					.getText();
			String mo = dr.findElement(By.xpath("//html/body/div[4]/div/div[2]/ul/li[1]")).getText();
			String ds = dr.findElement(By.xpath("//html/body/div[4]/div/div[2]/ul/li[2]")).getText();
			String la = dr.findElement(By.xpath("//html/body/div[4]/div/div[2]/ul/li[3]")).getText();
			String em = dr.findElement(By.xpath("//html/body/div[4]/div/div[2]/ul/li[4]")).getText();
			String in = dr.findElement(By.xpath("//html/body/div[4]/div/div[2]/ul/li[5]")).getText();
			String en = dr.findElement(By.xpath("//html/body/div[4]/div/div[2]/ul/li[6]")).getText();
			String pu = dr.findElement(By.xpath("//html/body/div[4]/div/div[2]/ul/li[7]")).getText();
			
			mo = mo.substring(mo.length() - 4);
			
			ds = ds.substring(ds.length() - 4);
			
			la = la.substring(la.length() - 4);
			
			em = em.substring(em.length() - 4);
			
			in = in.substring(in.length() - 4);
			
			en = en.substring(en.length() - 4);
			
			pu = pu.substring(pu.length() - 4);
			
			String test = "100";
			dr.findElement(By.xpath("//html/body/div[4]/div/div[3]/button")).click();

			dr.findElement(By.cssSelector("a[href='M1.jsp']")).click();
			for (int i = 1; i < 30; i++) {
				if (dr.findElement(
						By.xpath("//html/body/div[2]/div/div/div/div/div[2]/table/tbody/tr[" + i + "]/td[3]")).getText()
						.equals(name)) {
					if (dr.findElement(
							By.xpath("//html/body/div[2]/div/div/div/div/div[2]/table/tbody/tr[" + i + "]/td[2]"))
							.getText().equals(topic)) {
						if (!dr.findElement(
								By.xpath("//html/body/div[2]/div/div/div/div/div[2]/table/tbody/tr[" + i + "]/td[4]"))
								.getText().equals(mo))
							throw new Exception("motivation coefficients does not match");
						if (!dr.findElement(
								By.xpath("//html/body/div[2]/div/div/div/div/div[2]/table/tbody/tr[" + i + "]/td[5]"))
								.getText().equals(ds))
							throw new Exception("digital skills coefficients does not match");
						if (!dr.findElement(
								By.xpath("//html/body/div[2]/div/div/div/div/div[2]/table/tbody/tr[" + i + "]/td[6]"))
								.getText().equals(la))
							throw new Exception("learning ability coefficients does not match");
						if (!dr.findElement(
								By.xpath("//html/body/div[2]/div/div/div/div/div[2]/table/tbody/tr[" + i + "]/td[7]"))
								.getText().equals(em))
							throw new Exception("e-materials coefficients does not match");
						if (!dr.findElement(
								By.xpath("//html/body/div[2]/div/div/div/div/div[2]/table/tbody/tr[" + i + "]/td[8]"))
								.getText().equals(in))
							throw new Exception("instructor coefficients does not match");
						if (!dr.findElement(
								By.xpath("//html/body/div[2]/div/div/div/div/div[2]/table/tbody/tr[" + i + "]/td[9]"))
								.getText().equals(en))
							throw new Exception("e-environment coefficients does not match");
						if (!dr.findElement(
								By.xpath("//html/body/div[2]/div/div/div/div/div[2]/table/tbody/tr[" + i + "]/td[10]"))
								.getText().equals(pu))
							throw new Exception("predicted usage coefficients does not match");
						
						break;
					}
				}
			}
			System.out.println("Test finished successfully");
			dr.close();
		} catch (Exception e) {
			fail(e.getMessage());
		}

	}
}
