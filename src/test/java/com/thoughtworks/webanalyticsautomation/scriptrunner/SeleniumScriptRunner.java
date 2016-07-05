package com.thoughtworks.webanalyticsautomation.scriptrunner;

import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class SeleniumScriptRunner implements ScriptRunner {
  private final Logger logger = Logger.getLogger(getClass());
  private final WebDriver driver;
  private final String CLOSE_DEBUGGER = "//input[@name='close2']";

  public SeleniumScriptRunner(final WebDriver selenium) {
    this.driver = selenium;
  }

  public String getHTMLSourceByExecutingScript(final String OMNITURE_DEBUGGER_URL,
      final String OMNITURE_DEBUGGER_WINDOW_TITLE,
      final String OMNITURE_DEBUGGER_URL_DECODE_CHECKBOX) {
    try {
      logger.info("Running script: " + OMNITURE_DEBUGGER_URL);
      // driver.runScript(OMNITURE_DEBUGGER_URL);
      executeScript(OMNITURE_DEBUGGER_URL);
      Thread.sleep(5000);
      logger.debug("*** Debugger window title: " + driver.getTitle());
      switchToWindowUsingTitle(OMNITURE_DEBUGGER_WINDOW_TITLE);
      driver.findElement(By.name(OMNITURE_DEBUGGER_URL_DECODE_CHECKBOX)).click();
      logger.debug("*** SeleniumScriptDebugger \n HTML source\n" + driver.getPageSource());

    } catch (Exception e) {
      logger.info("NOT able to open " + OMNITURE_DEBUGGER_WINDOW_TITLE);
    }
    closeDebugger();
    return driver.getPageSource();
  }

  private void closeDebugger() {
    logger.info("Close Omniture Debugger.");
    driver.findElement(By.xpath(CLOSE_DEBUGGER)).click();
    logger.info("Select parent window.");
    switchToWindowUsingTitle("");
  }

  private void executeScript(final String script) {
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript(script);
  }

  private boolean switchToWindowUsingTitle(final String title) {
    String currentWindow = driver.getWindowHandle();
    Set<String> availableWindows = driver.getWindowHandles();
    if (!availableWindows.isEmpty()) {
      for (String windowId : availableWindows) {
        if (driver.switchTo().window(windowId).getTitle().equals(title)) {
          return true;
        } else {
          driver.switchTo().window(currentWindow);
        }
      }
    }
    return false;
  }
}
