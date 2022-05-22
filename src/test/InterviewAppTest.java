import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class InterviewAppTest {
    WebDriver driver;

    @BeforeMethod
    public void setUp(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @Test (testName = "IN-1 pageTitle", description = "Title of page should be \"Interview App\"")
    public void TestPageTitle() {
        driver.get("https://interview-prep-test.herokuapp.com/");
        //username
        driver.findElement(By.name("email")).sendKeys("test@yahoo.com");
        //password
        driver.findElement(By.name("password")).sendKeys("test123");
        //login button
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        String expectedResult = "Interview App";
        Assert.assertEquals(driver.getTitle(),expectedResult);
    }
    @Test (testName = "IN-2 userAccess", description = "As a user, I should be able to see only \"Sign out\" button from nav bar. User should not have access to \"Manage Access\" button")
    public void TestUserAccess(){
        driver.get("https://interview-prep-test.herokuapp.com/");
        //username
        driver.findElement(By.name("email")).sendKeys("test@yahoo.com");
        //password
        driver.findElement(By.name("password")).sendKeys("test123");
        //login button
        driver.findElement(By.xpath("//button[@type='submit']")).click();

       WebElement signOutBtn = driver.findElement(By.xpath("//u[text()='Sign out']"));
        Assert.assertEquals(signOutBtn.isDisplayed(), true);

        //Manage Access is not displayed


    }
    @Test (testName = "IN-3 Default dashboards", description = "As a user, there should always be 3 dashboards present: All Topics, Coding, Soft Skills")
    public void TestDefaultDashboards(){
        driver.get("https://interview-prep-test.herokuapp.com/");
        //username
        driver.findElement(By.name("email")).sendKeys("test@yahoo.com");
        //password
        driver.findElement(By.name("password")).sendKeys("test123");
        //login button
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        WebElement allTopicsDashboard = driver.findElement(By.xpath("//*[text()='All Topics']"));
        WebElement codingDashboard = driver.findElement(By.xpath("//*[text()='Coding']"));
        WebElement softSkillsDashboard = driver.findElement(By.xpath("//*[text()='Soft skills']"));

        Assert.assertEquals(allTopicsDashboard.isDisplayed(), true);
        Assert.assertEquals(codingDashboard.isDisplayed(), true);
        Assert.assertEquals(softSkillsDashboard.isDisplayed(), true);
    }
    @Test (testName = "IN-4 InterviewRelatedStatements",description = "As a user I should have an option to add a statement in Do's and Dont's sections. Statements should take only letters and numbers.")
    public void InterviewRelatedStatements() throws InterruptedException {
        driver.get("https://interview-prep-test.herokuapp.com/");
        //username
        driver.findElement(By.name("email")).sendKeys("test@yahoo.com");
        //password
        driver.findElement(By.name("password")).sendKeys("test123");
        //login button
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        //Add do button
        driver.findElement(By.xpath("(//button[@class='btn btn-success badge-pill newbtn mb-3'])[1]")).click();
        driver.findElement(By.id("inputArea1")).sendKeys("TestingDo");
        driver.findElement(By.xpath("//*[text()='Enter']")).click();

        //Add don't button
        driver.findElement(By.xpath("(//button[@class='btn btn-success badge-pill newbtn mb-3'])[2]")).click();
        driver.findElement(By.id("inputArea2")).sendKeys("TestingDon't");
        driver.findElement(By.xpath("//*[text()='Enter']")).click();

        Thread.sleep(3000);
    }
    @AfterMethod
    public void tearDown(){
        driver.close();
    }
}
