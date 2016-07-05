package com.thoughtworks.webanalyticsautomation.scriptrunner.helper;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.SkipException;

import com.thoughtworks.webanalyticsautomation.common.BROWSER;
import com.thoughtworks.webanalyticsautomation.common.Utils;
import com.thoughtworks.webanalyticsautomation.runUtils.UIDriverThreadRunner;

/**
 * Created by: Anand Bagmar Email: abagmar@gmail.com Date: Dec 29, 2010 Time: 1:11:18 PM
 * <p/>
 * Copyright 2010 Anand Bagmar (abagmar@gmail.com). Distributed under the Apache 2.0 License
 */

public class SeleniumScriptRunnerHelper extends ScriptRunnerHelper {

  private UIDriverThreadRunner uiDriverThreadRunner;
  private String UIDriver_BROWSER = "*";
  private final String DRIVER_HOST = "localhost";
  private final String TIMEOUT = "180000";
  private WebDriver driver;

  public SeleniumScriptRunnerHelper(final Logger logger, final BROWSER browser,
      final String baseUrl) {
    super(logger, browser, baseUrl);
    UIDriver_BROWSER = "*" + browser.name();
    UIDriver_BROWSER = "FireFox";
  }

  @Override
  public void startDriver() {
    String os = System.getProperty("os.name").toLowerCase();
    logger.info("Starting Selenium on OS: " + os + " for browser: " + browser.name());
    if (browser.equals(BROWSER.iehta) && (!os.contains("win"))) {
      throw new SkipException(
          "Skipping this test as Internet Explorer browser is NOT available on " + os);
    }

    String depCommand = "java " + " -jar " + Utils.getAbsolutePath(new String[] {"lib", "test",
        "webTestingFrameworks", "webdriver", "selenium-server-standalone-2.40.0.jar"});
    String command = "java " + " -jar "
        + Utils.getAbsolutePath(new String[] {"lib", "selenium-server-standalone-2.40.0.jar"});
    command = command + " -port 4454";
    logger.info(command);
    // this.uiDriverThreadRunner = new UIDriverThreadRunner(logger);
    // this.uiDriverThreadRunner.runInThread(command);
    int DRIVER_SERVER_PORT = 4454;
    driver = getDriver(UIDriver_BROWSER);
    driver.get(BASE_URL);
  }

  @Override
  public void stopDriver() {
    logger.info("Stopping driver.");
    try {
      if (null != driver) {
        driver.close();
      }
      if (null != uiDriverThreadRunner) {
        uiDriverThreadRunner.stop();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public WebDriver getDriver(final String browserName) {
    getBrowerSpecificDriver(browserName);
    driver.manage().window().maximize();
    return driver;
  }

  private void getBrowerSpecificDriver(final String browserName) {
    if (browserName.equalsIgnoreCase("FireFox")) {
      driver = new FirefoxDriver();
    }
    if (browserName.equalsIgnoreCase("IE")) {
      System.setProperty("webdriver.ie.driver", "IEDriverServer.exe");
      driver = new InternetExplorerDriver();
    }
    if (browserName.equalsIgnoreCase("Chrome")) {
      System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
      driver = new ChromeDriver();
    }

  }

  @Override
  public Object getDriverInstance() {
    return driver;
  }
}
