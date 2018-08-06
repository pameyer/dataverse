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
import com.jayway.restassured.response.Response;

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
		Response createUser = UtilIT.createRandomUser();
		String username = UtilIT.getUsernameFromResponse(createUser); // the password *appears* be the same as the username
		//also appears that first and last names are same as username
		WebElement element = driver.findElement(By.id("loginpage-header"));
		element.click();
		String title_expected = "Log In - Root";
		String title_observed = driver.getTitle();
		assertEquals( title_expected, title_observed);
		// loving these JSF element IDs...
		String login_name_id="loginForm:credentialsContainer:0:credValue";
		String login_pass_id="loginForm:credentialsContainer:1:sCredValue";
		String login_submit_id="loginForm:login";
		WebElement un = driver.findElement(By.id(login_name_id));
		un.sendKeys( username );
		WebElement pw = driver.findElement(By.id(login_pass_id));
		pw.sendKeys( username );
		WebElement sm = driver.findElement(By.id(login_submit_id));
		sm.click();
		// if we're logged in, we should be back at the root
		assertEquals("Root", driver.getTitle() );
		// and we should be showing the username
		WebElement udi=driver.findElement(By.id("userDisplayInfoTitle"));//good element IDs are nice
		String uni = udi.getText();
		String uni_e = String.format("%s %s",username,username);
		assertEquals(uni, uni_e);
	}
}
