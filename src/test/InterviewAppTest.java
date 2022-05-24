import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class InterviewAppTest {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get("https://interview-prep-test.herokuapp.com/");
        //username
        driver.findElement(By.name("email")).sendKeys("test@yahoo.com");
        //password
        driver.findElement(By.name("password")).sendKeys("test123");
        //login button
        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }

    @Test(testName = "IN-1 pageTitle", description = "Title of page should be \"Interview App\"")
    public void TestPageTitle() {
        String expectedResult = "Interview App";
        Assert.assertEquals(driver.getTitle(), expectedResult);
    }

    @Test(testName = "IN-2 userAccess", description = "As a user, I should be able to see only \"Sign out\" button from nav bar. User should not have access to \"Manage Access\" button")
    public void TestUserAccess() {
        WebElement signOutBtn = driver.findElement(By.xpath("//u[text()='Sign out']"));
        Assert.assertEquals(signOutBtn.isDisplayed(), true);

    }

    @Test(testName = "IN-3 Default dashboards", description = "As a user, there should always be 3 dashboards present: All Topics, Coding, Soft Skills")
    public void TestDefaultDashboards() {
        WebElement allTopicsDashboard = driver.findElement(By.xpath("//*[text()='All Topics']"));
        WebElement codingDashboard = driver.findElement(By.xpath("//*[text()='Coding']"));
        WebElement softSkillsDashboard = driver.findElement(By.xpath("//*[text()='Soft skills']"));

        Assert.assertEquals(allTopicsDashboard.isDisplayed(), true);
        Assert.assertEquals(codingDashboard.isDisplayed(), true);
        Assert.assertEquals(softSkillsDashboard.isDisplayed(), true);
    }

    @Test(testName = "IN-4 InterviewRelatedStatements", description = "As a user I should have an option to add a statement in Do's and Dont's sections. Statements should take only letters and numbers.")
    public void InterviewRelatedStatements() throws InterruptedException {
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

    @Test(testName = "in-8 searchOption", description = "I would like an option to search for certain question based on any given word as a criteria")
    public void testSearchOption() {
        wait = new WebDriverWait(driver, 20);
        //Click on "Coding" dashboard
        driver.findElement(By.xpath("//*[text()='Coding']")).click();
        //Verify you are on the "Coding" page
        Assert.assertEquals(driver.findElement(By.xpath("//div[@class='row dash-header']/h1")).getText(), "Coding");
        //click Enter new Question
        driver.findElement(By.xpath("//button[text()='Enter new question ']")).click();
        // input a new question in "Your Question" field
        driver.findElement(By.xpath("//textarea[@class='form-control']")).sendKeys("Why nine is afraid of seven because seven ate nine");
        //click "Enter" button
        driver.findElement(By.xpath("//button[text()='Enter']")).click();
        //verify user input was added
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='col-md-8']")));
        Assert.assertEquals(driver.findElement(By.xpath("//div[@class='col-md-8']")).getText(), "Why nine is afraid of seven because seven ate nine");
        //Go back to the previous page
        driver.navigate().back();


        //Click "Soft skills" dashboards
        driver.findElement(By.xpath("//*[text()='Soft skills']")).click();
        //Verify you are on the "Soft Skills" page
        Assert.assertEquals(driver.findElement(By.xpath("//div[@class='row dash-header']/h1")).getText(), "Softskill");
        //click Enter new Question
        driver.findElement(By.xpath("//button[text()='Enter new question ']")).click();
        // input a new question in "Your Question" field
        driver.findElement(By.xpath("//input[@class='form-control']")).sendKeys("seven ate nine");
        //click "Enter" button
        driver.findElement(By.xpath("//button[text()='Enter']")).click();
        //verify user input was added
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='col-md-8']")));
        Assert.assertEquals(driver.findElement(By.xpath("//div[@class='col-md-8']")).getText(), "seven ate nine");
        //Go back to the previous page
        driver.navigate().back();

        //click on "All Topics" dashboard
        driver.findElement(By.xpath("//*[text()='All Topics']")).click();
        //Verify you are on the "All Topics" page
        Assert.assertEquals(driver.findElement(By.xpath("//div[@class='row dash-header']/h1")).getText(), "All Topics");
        //Grab All options displayed before inputting search criteria
        List<WebElement> optionsBeforeEnteringCriteria = driver.findElements(By.xpath("//div[@class='col-md-8']"));
        // input search criteria in "Search" field
        driver.findElement(By.xpath("//input[@class='form-control m-2']")).sendKeys("seven");
        //press search button
        driver.findElement(By.xpath("//button[@class='btn btn-primary']")).click();

        // Verify list has correct results based on the given criteria
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='col-md-8']")));
        List<WebElement> optionsAfterEnteringCriteria = driver.findElements(By.xpath("//div[@class='col-md-8']"));
        for (WebElement each : optionsAfterEnteringCriteria) {
            Assert.assertTrue(each.getText().contains("seven"));
        }

        //Verify that "Show all" btn brings back all questions to view removing the filter that was applied before
        driver.findElement(By.xpath("//*[text()='Show all']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='col-md-8']")));
        List<WebElement> optionsAfterClickingShowAllBtn = driver.findElements(By.xpath("//div[@class='col-md-8']"));
        for (int i = 0; i < optionsBeforeEnteringCriteria.size(); i++) {
            try {
                Assert.assertEquals(optionsBeforeEnteringCriteria.get(i).getText(), optionsAfterClickingShowAllBtn.get(i).getText());
            } catch (StaleElementReferenceException e) {
            }
        }

        //Negative scenario search criteria can't be more 40 characters
        driver.findElement(By.xpath("//input[@class='form-control m-2']")).sendKeys("Why nine is afraid of seven because seven");
        //press search button
        driver.findElement(By.xpath("//button[@class='btn btn-primary']")).click();
        // Verify list doesn't show any results with the given criteria
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='col-md-8']")));
        List<WebElement> negativeScenario = driver.findElements(By.xpath("//div[@class='col-md-8']"));
        for (WebElement each : negativeScenario) {
            Assert.assertFalse(each.getText().contains("Why nine is afraid of seven because seven"));
        }
    }

    @AfterMethod
    public void tearDown() {
        driver.close();
    }

}
