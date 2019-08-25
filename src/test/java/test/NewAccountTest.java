package test;

import java.util.List;
import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import page.ListAccountPage;
import page.LoginPage;
import page.NewAccountPage;
import page.SideNavigation;
import util.BrowserFactory;
import util.ExcelReader;

public class NewAccountTest {

	ExcelReader reader = new ExcelReader("./data/data.xlsx");
	String username = reader.getCellData("Sheet1", "UserName", 2);
	String password = reader.getCellData("Sheet1", "Password", 2);
	String accountTitle = reader.getCellData("Sheet1", "AccountTitle", 2);
	String balance = reader.getCellData("Sheet1", "InitialBalance", 2);

	// Test Case: Open a new account
	@Test
	public void OpenANewAccount() throws InterruptedException {
		WebDriver driver = BrowserFactory.startBrowser();
		LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
		loginPage.login(username, password);

		SideNavigation sidenav = PageFactory.initElements(driver, SideNavigation.class);
		sidenav.goToNewAccountPage();

		Random rnd = new Random();
		String expectedTitle = accountTitle + rnd.nextInt(999);
		System.out.println("Expected dat: " + expectedTitle);

		NewAccountPage newAccountPage = PageFactory.initElements(driver, NewAccountPage.class);
		newAccountPage.fillInTheNewAccountForm(expectedTitle, reader.getCellData("Sheet1", "Description", 2), balance);

		// Get the list of column data from the page class
		ListAccountPage listAccountPage = PageFactory.initElements(driver, ListAccountPage.class);
		List<String> columnData = listAccountPage.getColumnDataFor("Account");

		Assert.assertTrue(isDataPresent(expectedTitle,columnData ), "New Account did not post!!!!");
	}

	private boolean isDataPresent(String expectedTitle, List<String> columnData) {
		for(String cellData:columnData ) {
			if(cellData.equalsIgnoreCase(expectedTitle)) {
				System.out.println("Displayed Data: " + cellData);
				return true;
			}
		}
		return false;
	}
}
