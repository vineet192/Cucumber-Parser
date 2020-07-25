package StepDefinitions;

import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import cucumber_parser.TestNGRunner;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/*
 * This class contains definitions for steps to perform a simple login on the html pages in WebContent folder.
 * Driver used is chromedriver for chrome 83.0.x
 */
public class LoginSteps extends TestNGRunner {

	WebElement username;
	WebElement password;
	WebElement loginButton;

	@Given("user is on login page")
	public void user_is_on_login_page() {

		System.out.println("Navigating to login page");
		driver.get("http://localhost:8080/cucumber_parser/login.html");

		System.out.println("Current url is " + driver.getCurrentUrl());

	}

	@When("user enters {string} and {string}")
	public void user_enters_and(String string, String string2) {
		// Write code here that turns the phrase above into concrete actions

		WebDriverWait wait = new WebDriverWait(driver, 4000);
		wait.until(ExpectedConditions.visibilityOfElementLocated((By.id("username"))));

		username = driver.findElement(By.id("username"));
		password = driver.findElement(By.id("password"));
		System.out.println("Entering username and password as " + username + " & " + password + "\n");

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
		String actualUrl = null;
		try {
			actualUrl = driver.getCurrentUrl();
		} catch (UnhandledAlertException e) {
			System.out.println("Wrong username or password");
		}
		String expectedUrl = "http://localhost:8080/cucumber_parser/homepage.html";

		Assert.assertEquals(actualUrl, expectedUrl);
	}
}
