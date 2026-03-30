package com.juaracoding.evan;

// =========================
// IMPORT LIBRARY
// =========================

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class AddToCartTest {

    // =========================
    // INISIALISASI DRIVER
    // =========================
    static WebDriver driver;

    // =========================
    // INISIALISASI EXCEL
    // =========================
    static Workbook workbook = new XSSFWorkbook();
    static Sheet sheet = workbook.createSheet("SIT");

    static int rowNum = 1;

    // =========================
    // DATA LOGIN
    // =========================
    static String username = "standard_user";
    static String password = "secret_sauce";

    // =========================
    // MAIN METHOD (RUN JAVA)
    // =========================
    public static void main(String[] args) {

        AddToCartTest test = new AddToCartTest();

        test.setup();
        test.testLogin();
        test.testAddToCart();
        test.testRemoveItem();
        test.testCheckout();
        test.testCheckoutNegative();
        test.tearDown();
    }

    // =========================
    // SETUP
    // =========================
    @BeforeClass
    public void setup() {

        System.out.println("===== START AUTOMATION TEST =====");

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        driver.get("https://www.saucedemo.com/");
        driver.manage().window().maximize();

        createHeader();

        delay();
    }

    // =========================
    // TEARDOWN
    // =========================
    @AfterClass
    public void tearDown() {

        System.out.println("===== END AUTOMATION TEST =====");

        driver.quit();
        saveExcel();
    }

    // =========================
    // DELAY
    // =========================
    public static void delay() {

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
    }

    // =========================
    // TEST LOGIN
    // =========================
    @Test(priority = 1)
    public void testLogin() {

        System.out.println("\n[STEP] LOGIN TEST");

        String testCaseId = "SIT-001";
        String testName = "Login Test";

        try {

            System.out.println("[INFO] Input Username");
            driver.findElement(By.id("user-name")).sendKeys(username);
            delay();

            System.out.println("[INFO] Input Password");
            driver.findElement(By.id("password")).sendKeys(password);
            delay();

            System.out.println("[INFO] Click Login");
            driver.findElement(By.id("login-button")).click();
            delay();

            boolean success =
                    driver.getCurrentUrl().contains("inventory");

            if (success) {

                System.out.println("[RESULT] LOGIN SUCCESS");
                writeToExcel(testCaseId, testName, "SUCCESS");

            } else {

                System.out.println("[RESULT] LOGIN FAILED");
                writeToExcel(testCaseId, testName, "FAIL");
            }

            Assert.assertTrue(success);

        } catch (Exception e) {

            System.out.println("[RESULT] LOGIN FAILED");
            writeToExcel(testCaseId, testName, "FAIL");

            Assert.fail();
        }
    }

    // =========================
    // TEST ADD TO CART
    // =========================
    @Test(priority = 2, dependsOnMethods = "testLogin")
    public void testAddToCart() {

        System.out.println("\n[STEP] ADD TO CART");

        String testCaseId = "SIT-002";
        String testName = "Add To Cart";

        try {

            System.out.println("[INFO] Click Add To Cart");
            driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
            delay();

            String badge =
                    driver.findElement(By.className("shopping_cart_badge")).getText();

            if (badge.equals("1")) {

                System.out.println("[RESULT] ADD TO CART SUCCESS");
                writeToExcel(testCaseId, testName, "SUCCESS");

            } else {

                System.out.println("[RESULT] ADD TO CART FAILED");
                writeToExcel(testCaseId, testName, "FAIL");
            }

            Assert.assertEquals(badge, "1");

        } catch (Exception e) {

            System.out.println("[RESULT] ADD TO CART FAILED");
            writeToExcel(testCaseId, testName, "FAIL");

            Assert.fail();
        }
    }

    // =========================
    // TEST REMOVE ITEM
    // =========================
    @Test(priority = 3, dependsOnMethods = "testAddToCart")
    public void testRemoveItem() {

        System.out.println("\n[STEP] REMOVE ITEM");

        String testCaseId = "SIT-003";
        String testName = "Remove Item";

        try {

            System.out.println("[INFO] Click Remove");
            driver.findElement(By.id("remove-sauce-labs-backpack")).click();
            delay();

            boolean empty =
                    driver.findElements(By.className("shopping_cart_badge")).isEmpty();

            if (empty) {

                System.out.println("[RESULT] REMOVE SUCCESS");
                writeToExcel(testCaseId, testName, "SUCCESS");

            } else {

                System.out.println("[RESULT] REMOVE FAILED");
                writeToExcel(testCaseId, testName, "FAIL");
            }

            Assert.assertTrue(empty);

        } catch (Exception e) {

            System.out.println("[RESULT] REMOVE FAILED");
            writeToExcel(testCaseId, testName, "FAIL");

            Assert.fail();
        }
    }

    // =========================
    // TEST CHECKOUT
    // =========================
    @Test(priority = 4, dependsOnMethods = "testRemoveItem")
    public void testCheckout() {

        System.out.println("\n[STEP] CHECKOUT POSITIVE");

        String testCaseId = "SIT-004";
        String testName = "Checkout";

        try {

            System.out.println("[INFO] Add Item");
            driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
            delay();

            System.out.println("[INFO] Open Cart");
            driver.findElement(By.className("shopping_cart_link")).click();
            delay();

            System.out.println("[INFO] Click Checkout");
            driver.findElement(By.id("checkout")).click();
            delay();

            driver.findElement(By.id("first-name")).sendKeys(username);
            delay();

            driver.findElement(By.id("last-name")).sendKeys(password);
            delay();

            driver.findElement(By.id("postal-code")).sendKeys(generateZip());
            delay();

            driver.findElement(By.id("continue")).click();
            delay();

            driver.findElement(By.id("finish")).click();
            delay();

            boolean success =
                    driver.findElement(By.className("complete-header"))
                            .getText()
                            .contains("Thank you");

            if (success) {

                System.out.println("[RESULT] CHECKOUT SUCCESS");
                writeToExcel(testCaseId, testName, "SUCCESS");

            } else {

                System.out.println("[RESULT] CHECKOUT FAILED");
                writeToExcel(testCaseId, testName, "FAIL");
            }

            Assert.assertTrue(success);

        } catch (Exception e) {

            System.out.println("[RESULT] CHECKOUT FAILED");
            writeToExcel(testCaseId, testName, "FAIL");

            Assert.fail();
        }
    }

    // =========================
    // TEST CHECKOUT NEGATIVE
    // =========================
    @Test(priority = 5, dependsOnMethods = "testCheckout")
    public void testCheckoutNegative() {

        System.out.println("\n[STEP] CHECKOUT NEGATIVE");

        String testCaseId = "SIT-005";
        String testName = "Checkout Negative";

        try {

            driver.get("https://www.saucedemo.com/inventory.html");
            delay();

            driver.findElement(By.id("add-to-cart-sauce-labs-bike-light")).click();
            delay();

            driver.findElement(By.className("shopping_cart_link")).click();
            delay();

            driver.findElement(By.id("checkout")).click();
            delay();

            driver.findElement(By.id("continue")).click();
            delay();

            boolean error =
                    driver.findElement(By.className("error-message-container"))
                            .isDisplayed();

            if (error) {

                System.out.println("[RESULT] NEGATIVE TEST SUCCESS");
                writeToExcel(testCaseId, testName, "SUCCESS");

            } else {

                System.out.println("[RESULT] NEGATIVE TEST FAILED");
                writeToExcel(testCaseId, testName, "FAIL");
            }

            Assert.assertTrue(error);

        } catch (Exception e) {

            System.out.println("[RESULT] NEGATIVE TEST FAILED");
            writeToExcel(testCaseId, testName, "FAIL");

            Assert.fail();
        }
    }