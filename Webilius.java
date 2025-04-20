package webilius;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class Webilius {

    WebDriver driver;
    static ExtentReports extent;
    static ExtentTest test;

    @BeforeSuite
    public void setupReport() {
        ExtentSparkReporter reporter = new ExtentSparkReporter("target/LoginReport.html");
        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    @AfterSuite
    public void tearDownReport() {
        extent.flush();
    }

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://www.saucedemo.com/");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(priority = 1)
    public void validLoginTest() {
        test = extent.createTest("Valid Login Test");

        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        boolean loginSuccess = driver.getCurrentUrl().contains("inventory");
        Assert.assertTrue(loginSuccess, "Should redirect to inventory page");
        test.pass("Login successful and user landed on inventory page");
    }

    @Test(priority = 2)
    public void invalidLoginTest() {
        test = extent.createTest("Invalid Login Test");

        driver.findElement(By.id("user-name")).sendKeys("wrong_user");
        driver.findElement(By.id("password")).sendKeys("wrong_pass");
        driver.findElement(By.id("login-button")).click();

        WebElement errorMsg = driver.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(errorMsg.getText().contains("Username and password do not match"));
        test.pass("Correct error shown for invalid login");
    }

    @Test(priority = 3)
    public void emptyFieldsLoginTest() {
        test = extent.createTest("Empty Fields Login Test");

        driver.findElement(By.id("login-button")).click();

        WebElement errorMsg = driver.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(errorMsg.getText().contains("Username is required"));
        test.pass("Correct error shown for empty username");
    }
}
