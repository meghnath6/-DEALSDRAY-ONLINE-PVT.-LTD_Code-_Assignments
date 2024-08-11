package com.Practice;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.time.Duration;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FileUploadTest {

    static String url = "https://demo.dealsdray.com/";
    private final static String userName = "prexo.mis@dealsdray.com";
    private final static String password = "prexo.mis@dealsdray.com";
    private static final String SCREENSHOT_FOLDER = "C:\\Users\\lonel\\eclipse-workspace\\Practice\\ScreenShots";

    public static void main(String[] args) {
        WebDriver driver = null;
        try {
            driver = getDriver();
            driver.manage().window().maximize();
            driver.get(url);
            Thread.sleep(2000);
            
            // Logging in to the application
            WebElement usernameTextbox = driver.findElement(By.xpath("//input[@name='username']"));
            waitUntilElementDisplayed(usernameTextbox, driver);
            highLightElement(driver, usernameTextbox);
            SingleClick(usernameTextbox);
            Thread.sleep(2000);
            usernameTextbox.sendKeys(userName);

            WebElement passWordTextBox = driver.findElement(By.xpath("//input[@name='password']"));
            highLightElement(driver, passWordTextBox);
            SingleClick(passWordTextBox);
            Thread.sleep(2000);
            passWordTextBox.sendKeys(password);

            WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(),'Login')]"));
            SingleClick(loginButton);

            // Handling Orders Dropdown and Navigating to Add Bulk Orders
            try {
                WebElement ordersDropdown = driver.findElement(By.xpath(
                    "//div[contains(@class,'MuiCard-root')][1]"));
                DoubleClickUsingActionClass(driver, ordersDropdown);
                Thread.sleep(4000);
                System.out.println("Performed double click");
                
                WebElement addBulkOrderButton = driver.findElement(By.xpath("//button[text()='Add Bulk Orders']"));
                SingleClick(addBulkOrderButton);
            } catch (Exception e) {
                System.out.println("Click action failed due to: " + e.getMessage());
                String altUrl = "https://demo.dealsdray.com/mis/orders";
                driver.navigate().to(altUrl);
                Thread.sleep(4000);
                if (driver.findElements(By.xpath("//button[text()='Add Bulk Orders']")).size() > 0) {
                    WebElement addBulkOrderButton = driver.findElement(By.xpath("//button[text()='Add Bulk Orders']"));
                    DoubleClickUsingActionClass(driver, addBulkOrderButton);
                    Thread.sleep(8000);
                }
            }

            // File Upload using Robot class
            if (driver.findElements(By.xpath("//input[@type='file']")).size() > 0) {
                WebElement uploadButton = driver.findElement(By.xpath("//input[@type='file']"));
                waitUntilElementClickable(driver, uploadButton);
                highLightElement(driver, uploadButton);
                DoubleClickUsingActionClass(driver, uploadButton);

                // Using Robot class to handle file upload dialog
                uploadFileUsingRobot("C:\\Users\\lonel\\Downloads\\demo-data.xlsx");
                Thread.sleep(20000);
                takeScreenshot(driver, "AfterFileUpload");
            }

            // Click on the Import button if it exists
            if (driver.findElements(By.xpath("//button[contains(text(),'Import')]")).size() > 0) {
                WebElement importButton = driver.findElement(By.xpath("//button[contains(text(),'Import')]"));
                waitUntilElementClickable(driver, importButton);
                highLightElement(driver, importButton);
                DoubleClickUsingActionClass(driver, importButton);
                Thread.sleep(6000);
                takeScreenshot(driver, "AfterImport");
            }

            // Click on the Validate Data button if it exists
            if (driver.findElements(By.xpath("//button[contains(text(),'Validate Data')]")).size() > 0) {
                WebElement validateDataButton = driver.findElement(By.xpath("//button[contains(text(),'Validate Data')]"));
                waitUntilElementClickable(driver, validateDataButton);
                highLightElement(driver, validateDataButton);
                DoubleClickUsingActionClass(driver, validateDataButton);
                Thread.sleep(6000);
                takeScreenshot(driver, "AfterValidateData");
            }

        } catch (Exception e) {
            System.out.println("Failed due to: " + e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    // Method to initiate WebDriver
    private static WebDriver getDriver() {
        WebDriver driver = null;
        try {
            System.setProperty("webdriver.chrome.driver",
                    "C:\\Users\\lonel\\eclipse-workspace\\chromedriver-win64\\chromedriver.exe"); // Update path
            ChromeOptions chromeOptions = new ChromeOptions();
            driver = new ChromeDriver(chromeOptions);
        } catch (Exception e) {
            System.out.println("Failed due to: " + e.getMessage());
        }
        return driver;
    }

    // Method to wait until the element is displayed
    public static void waitUntilElementDisplayed(final WebElement webElement, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        ExpectedCondition<Boolean> elementIsDisplayed = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver arg0) {
                try {
                    return webElement.isDisplayed();
                } catch (NoSuchElementException | StaleElementReferenceException e) {
                    return false;
                }
            }
        };
        wait.until(elementIsDisplayed);
    }

    // Method to click on an element
    public static void SingleClick(WebElement element) {
        element.click();
    }

    // Method to highlight an element
    public static void highLightElement(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        js.executeScript("arguments[0].setAttribute('style','border: solid 2px white');", element);
    }

    // Method to wait until an element is clickable
    public static void waitUntilElementClickable(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    // Method to perform double-click using Action class
    public static void DoubleClickUsingActionClass(WebDriver driver, WebElement element) {
        Actions action = new Actions(driver);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
        action.doubleClick(element).build().perform();
    }

    // Method to upload a file using Robot class
    public static void uploadFileUsingRobot(String filePath) {
        try {
            // Copy the file path to the clipboard
            StringSelection selection = new StringSelection(filePath);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

            // Use Robot to paste the file path and press Enter
            Robot robot = new Robot();
            robot.delay(1000);  // Small delay before performing the actions
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            System.out.println("File upload successful.");
        } catch (Exception e) {
            System.out.println("File upload failed due to: " + e.getMessage());
        }
    }

    // Method to take a screenshot and save it with a specific name
    public static void takeScreenshot(WebDriver driver, String screenshotName) {
        try {
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshotFile, new File(SCREENSHOT_FOLDER + "\\" + screenshotName + ".png"));
            System.out.println("Screenshot saved: " + screenshotName);
        } catch (Exception e) {
            System.out.println("Failed to take screenshot: " + e.getMessage());
        }
    }
}
