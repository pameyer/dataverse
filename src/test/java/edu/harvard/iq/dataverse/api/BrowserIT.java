package edu.harvard.iq.dataverse.api;
import java.util.logging.Logger;
import com.jayway.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static junit.framework.Assert.assertEquals;

public class BrowserIT
{
	private static final Logger logger = Logger.getLogger( BrowserIT.class.getCanonicalName());
	private static WebDriver driver;

	@BeforeClass
	public static void setUpClass()
	{
		RestAssured.baseURI = UtilIT.getRestAssuredBaseUri();
		driver = new HtmlUnitDriver();
	}
	@AfterClass
	public static void tearDownClass()
	{
		driver.quit();
	}

	/**
	 * try to load the root dataverse in headless browser, assuming that it
	 * has already been published.
	 */
	@Test
	public void testLoadRootDataverse()
	{
		//System.out.format("baseURI=%s\n",RestAssured.baseURI);
		driver.get(RestAssured.baseURI);
		String title_expected = "Root";
		String title_observed = driver.getTitle();
		assertEquals( title_expected, title_observed);
	}

	/**
	 * try to login in.
	 * Eventually - for now, just try to load the login page
	 */
	@Test
	public void testLogin()
	{
		WebElement element = driver.findElement(By.id("loginpage-header"));
		element.click();
		String title_expected = "Log In - Root";
		String title_observed = driver.getTitle();
		assertEquals( title_expected, title_observed);
	}
}
