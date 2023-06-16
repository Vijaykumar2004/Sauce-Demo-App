package sauce_demo_testrunner;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import jxl.common.AssertionFailed;

public class Sauce_demo_test {

	WebDriver driver;
	String credentials = "F:\\Vijay\\Saucedemo\\Saucedemo credentials.xlsx";

	@BeforeClass
	void setup() {
		System.setProperty("webdriver.chrome.driver", "F:\\Vijay\\chrome\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("https://www.saucedemo.com/");
		// driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test(alwaysRun = true)
	void login() throws InterruptedException, IOException {

		// Excel file import
		FileInputStream F = new FileInputStream(credentials);
		XSSFWorkbook WB = new XSSFWorkbook(F);
		XSSFSheet S = WB.getSheetAt(0);
		Cell cell;
		for (int i = 1; i < 5; i++) {
			cell = S.getRow(i).getCell(0);
			String Username = cell.getStringCellValue();
			cell = S.getRow(i).getCell(1);
			String Password = cell.getStringCellValue();

		// sending input
			WebElement User = driver.findElement(By.xpath("//input[@type=\"text\"]"));
			User.sendKeys(Username);
			WebElement Pass = driver.findElement(By.xpath("//input[@type=\"password\"]"));
			Pass.sendKeys(Password);
			WebElement Login = driver.findElement(By.xpath("//input[@type=\"submit\"]"));
			Login.click();
	
		//Assertion-1 for Login
		String list_of_product = driver.findElement(By.xpath("//span[@class='title']")).getText();
		if (list_of_product.equalsIgnoreCase("Products")) {
			assertTrue(true);
			System.out.println("user name: " + Username + " login sucessfully");
		} else {
			assertTrue(false);
			System.out.println("user name: " + Username + " wrong user");
		}
		
		//filter
			WebElement Filter = driver.findElement(By.xpath("//select[@data-test=\"product_sort_container\"]"));
			Filter.click();
			WebElement LtoH = driver.findElement(By.xpath("//option[@value=\"lohi\"]"));
			LtoH.click();
		//Add to cart	
			List<WebElement> items = driver.findElements(By.xpath("//button[contains(text(),'Add to cart')]"));
			for (WebElement item : items) {
	            item.click();
	            }
			
			Thread.sleep(5000);
			
		//Assertion-2 for Items Added to cart	
			WebElement  items_in_cart = driver.findElement(By.cssSelector(".shopping_cart_badge"));
			String No_of_items = items_in_cart.getText();
			if(No_of_items.equalsIgnoreCase("6")) {
				assertTrue(true);
				System.out.println("No of items : "+No_of_items+" item add to cart sucessfully");
				}
				else{
					assertFalse(false);
					System.out.println("add to cart item assertion fail");
				}
		//view cart
			WebElement cart = driver.findElement(By.xpath("//a[@class='shopping_cart_link']"));
			cart.click();
			
			Thread.sleep(5000);
			
		//Getting list of items in cart
			List<WebElement> items_remove = driver.findElements(By.xpath("//div[@class='cart_item']//button[contains(text(),'Remove')]"));
			for(WebElement remove : items_remove) {
		//Getting item price
				WebElement price = remove.findElement(By.xpath("//div[@class='inventory_item_price']"));
				String pricetext = price.getText();
		//Parsing
				double f1 = Double.parseDouble(pricetext.replace("$",""));
		//Condition checking and removing items
				if(f1 < 15) {
				remove.click();
				}
			}
		//Assertion-3 for Removing Items from cart
			WebElement  itemsincart_after_remove = driver.findElement(By.cssSelector(".shopping_cart_badge"));
			String No_of_items_after_remove = itemsincart_after_remove.getText();
			if(No_of_items_after_remove.equalsIgnoreCase("4")) {
				assertTrue(true);
				System.out.println("less than $15  remover from cart sucessfully");
				}			
		
			//Go to cart
			WebElement cart_container = driver.findElement(By.xpath("//div[@id=\"shopping_cart_container\"]"));
			cart_container.click();
			
			//Checkout
			WebElement checkout = driver.findElement(By.xpath("//button[@name=\"checkout\"]"));
			checkout.click();
			
			//Enter details for checkout
			WebElement fname = driver.findElement(By.xpath("//input[@id=\"first-name\"]"));
			fname.sendKeys("Vijay");
			WebElement lname = driver.findElement(By.xpath("//input[@id=\"last-name\"]"));
			lname.sendKeys("Kumar");	
			
			
			String error = fname.getText();
			String error1 = lname.getText();
			
			if(error!=null&error1!=null) {
			
				
				WebElement Zipcode = driver.findElement(By.xpath("//input[@id=\"postal-code\"]"));
				Zipcode.sendKeys("64110");
					
				WebElement cont = driver.findElement(By.xpath("//input[@id='continue']"));
				cont.click();
			
				//finish
				WebElement finish = driver.findElement(By.xpath("//button[@id='finish']"));
				if(finish.isEnabled()){
				finish.click();
				
				//back to home
				WebElement Home = driver.findElement(By.xpath("//button[@id='back-to-products']"));
				Home.click();
				
				Thread.sleep(3000);
				
				//Menu
				WebElement Menu = driver.findElement(By.xpath("//button[@id=\"react-burger-menu-btn\"]"));
				Menu.click();
			
				//Logout
				WebElement Logout = driver.findElement(By.xpath("//a[@id=\"logout_sidebar_link\"]"));
				Logout.click();
			
				System.out.println("logout sucessfully");
			}
			else {
			
				//Menu
				WebElement Menu = driver.findElement(By.xpath("//button[@id=\"react-burger-menu-btn\"]"));
				Menu.click();
			
				//Logout
				WebElement Logout = driver.findElement(By.xpath("//a[@id=\"logout_sidebar_link\"]"));
				Logout.click();
			}
				}
	}
			}

	@AfterClass
	void teardown() {
		driver.quit();
	}
		
}