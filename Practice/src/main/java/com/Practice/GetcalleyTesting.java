package com.Practice;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GetcalleyTesting {

	// As per test case Scenario i am taking chrome and firefox browsers...
    private static final String BROWSERS[] = {"chrome", "firefox"};    // "safari"
    private static final String DESKTOP_RESOLUTIONS[] = {"1920x1080", "1366x768", "1536x864"};
    private static final String MOBILE_RESOLUTIONS[] = {"360x640", "414x896", "375x667"};
    private static final String DEVICE_TYPES[] = {"Desktop", "Mobile"};
    private static final String BASE_URL = "https://www.getcalley.com/page-sitemap.xml"; 
    
    // Creating a local older for storing the screen shorts.... 
    private static final String SCREENSHOT_FOLDER = "C:\\Users\\lonel\\eclipse-workspace\\Practice\\ScreenShots";
    
    // main method started
    public static void main(String[] args) throws IOException, InterruptedException {
        File screenshotDir = new File(SCREENSHOT_FOLDER);
        
        // checking for file directory existance.....
        if (!screenshotDir.exists()) screenshotDir.mkdirs();
        
        for (String browser : BROWSERS) {
            for (String deviceType : DEVICE_TYPES) {
            	
            	// checking the type of device and storeing in the resolutions[] array...
                String[] resolutions = deviceType.equals("Desktop") ? DESKTOP_RESOLUTIONS : MOBILE_RESOLUTIONS;
                
                for (String resolution : resolutions) {
                    WebDriver driver = createDriver(browser);
                    if (driver == null) continue;
                    try {
                        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                        String[] res = resolution.split("x");
                        int width = Integer.parseInt(res[0]);
                        int height = Integer.parseInt(res[1]);
                        driver.manage().window().setSize(new Dimension(width, height));
                        driver.get(BASE_URL);
                        Thread.sleep(500);
                        WebElement element = driver.findElement(By.xpath("//div[@id=\"content\"]/p[contains(text(),'This XML Sitemap contains')]"));
                        waitUntilElementDisplayed(element, driver);
                        highLightElement(driver, element);
                        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                        String screenshotPath = String.format("%s/%s/%s/%s-%s.png",
                                SCREENSHOT_FOLDER, deviceType, resolution, browser, timestamp);

                        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                        FileUtils.copyFile(screenshotFile, new File(screenshotPath));
                        
                        System.out.println("Screenshot taken Sucessfully for " + browser + " - " + deviceType + " - " + resolution);
                        
                        // Additional code to record video could be implemented here, using tools like VideoRecorder or third-party services

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        driver.quit();
                    }
                }
            }
        }
    }

    private static WebDriver createDriver(String browser) {
        switch (browser) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver", "C:\\Users\\lonel\\eclipse-workspace\\chromedriver-win64\\chromedriver.exe"); // Update path
                ChromeOptions chromeOptions = new ChromeOptions();
                return new ChromeDriver(chromeOptions);
            case "firefox":
                System.setProperty("webdriver.gecko.driver", "C:\\Users\\lonel\\eclipse-workspace\\gekodriver-win64\\geckodriver.exe"); // Update path
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                return new FirefoxDriver(firefoxOptions);
                
                
//            case "safari":
//                // SafariDriver is bundled with Safari browser
 //               System.setProperty("webdriver.safari.driver", "Keep the path from the system"); // Update path
//                return new SafariDriver();
            default:
                System.err.println("Unsupported browser: " + browser);
                return null;
        }
    }
	public static void waitUntilElementDisplayed(final WebElement webElement, WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(500));
		ExpectedCondition<Boolean> elementIsDisplayed = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver arg0) {
				try {
					webElement.isDisplayed();
					return true;
				} catch (NoSuchElementException e) {
					return false;
				} catch (StaleElementReferenceException f) {
					return false;
				}
			}
		};
		wait.until(elementIsDisplayed);
	}

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
}
