package com.kps.kenziethebot.model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.util.List;
import java.util.Vector;


public class Webdriver {

    /**
     * This class integrated the functionality of the selenium automation
     * framework into the project.
     */

    private static WebDriver driver;
    private static final String WEBDRIVER_PATH = new File("lib/chromedriver.exe").getAbsolutePath();
    private static List<Integer> openWindows = new Vector<>(); // for thread safety and to prevent deadlocks.


    public static void webDriverGet(String url) {

        new Thread(
                () -> {

                    System.setProperty("webdriver.chrome.driver", WEBDRIVER_PATH);

                    driver = new ChromeDriver();

                    System.out.println("{+} Opening new browser window.");
                    openWindows.add(1);

                    driver.get(url);
                }
        ).start();
    }

    public static void webDriverSignIn(String username, String password) {

        new Thread(
                () -> {

                    try {

                        System.out.println("{<+>} Auto login has been initialized.");

                        System.setProperty("webdriver.chrome.driver", WEBDRIVER_PATH);
                        driver = new ChromeDriver();

                        System.out.println("{+} Opening new browser window.");
                        openWindows.add(1);

                        driver.get("https://gmail.com");

                        Thread.sleep(2000);
                        driver.findElement(By.id("identifierId")).sendKeys(username);

                        Thread.sleep(2000);
                        driver.findElement(By.xpath("//*[@id=\"identifierNext\"]/content/span")).click();

                        Thread.sleep(2000);
                        driver.findElement(By.name("password")).sendKeys(password);

                        driver.findElement(By.xpath("//*[@id=\"passwordNext\"]/content")).click();

                        Thread.sleep(4000);
                        TTS.speak("welcome back ,sir");

                    } catch (InterruptedException ie) {

                        System.out.println("{X} Exception while logging into gmail");
                        System.out.println(ie.getMessage());
                    }
                }

        ).start();
    }

    public static WebDriver getDriver() {

        return driver;
    }

    public static void closeWindow() {

        if (openWindows.isEmpty()) {

            System.out.println("{*} All windows are already closed.");
            TTS.speak("no window is currently open.");

        } else {

            System.out.println("{*} Closing the recent window.");
            openWindows.remove(openWindows.size() - 1);

            TTS.speak("closing the window");
            driver.close();
        }
    }
}

