package cucumber_parser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


/*
 * This class contains definitions for steps to perform a simple login on the html pages in WebContent folder.
 * Driver used is chromedriver for chrome 83.0.x
 */
public class LoginSteps {

	WebDriver driver;
	WebElement username;
	WebElement password;
	WebElement loginButton;

	@Given("Browser is open")
	public void browser_is_open() {

		System.out.println("Opening the browser (Chrome)");
		System.setProperty("webdriver.chrome.driver", "C:/Users/V.X.V.K/eclipse-workspace/cucumber_parser/WebDrivers/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();

	}

	@And("user is on login page")
	public void user_is_on_login_page() {
		
		System.out.println("Navigating to login page");
		driver.get("http://localhost:8080/cucumber_parser/login.html");

	}

//	@When("user enters username and password")
//	public void user_enters_username_and_password() {
//
//		System.out.println("Entering username and password");
//		
//		username = driver.findElement(By.id("username"));
//		password = driver.findElement(By.id("password"));
//		username.sendKeys("exampleuser");
//		password.sendKeys("examplepass");
//
//	}
	
	@When("user enters {string} and {string}")
	public void user_enters_and(String string, String string2) {
	    // Write code here that turns the phrase above into concrete actions
		System.out.println("Entering username and password");
		
		username = driver.findElement(By.id("username"));
		password = driver.findElement(By.id("password"));
		username.sendKeys(string);
		password.sendKeys(string2);
	}

	@And("user clicks on login")
	public void userc_clicks_on_login() {

		System.out.println("Clicking on login");
		loginButton = driver.findElement(By.id("submit"));
		loginButton.click();

	}

	@Then("user is navigated to homepage")
	public void user_is_navigated_to_homepage() {

		String actualUrl = driver.getCurrentUrl();
		String expectedUrl = "http://localhost:8080/cucumber_parser/homepage.html";
		Assert.assertEquals(actualUrl, expectedUrl);
	}
}
