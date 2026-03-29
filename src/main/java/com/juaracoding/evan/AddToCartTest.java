package com.juaracoding.evan;

// Import Selenium
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

import io.github.bonigarcia.wdm.WebDriverManager;

public class AddToCartTest {

    // Inisialisasi WebDriver
    static WebDriver driver;

    // Inisialisasi Excel Workbook
    static Workbook workbook = new XSSFWorkbook();

    // Membuat Sheet Excel
    static Sheet sheet = workbook.createSheet("SIT");

    // Baris Excel dimulai dari 1
    static int rowNum = 1;

    // Username dan Password
    static String username = "standard_user";
    static String password = "secret_sauce";

    public static void main(String[] args) {

        // Setup Chrome Driver
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        // Membuat Header Excel
        createHeader();

        try {

            // Membuka Website SauceDemo
            driver.get("https://www.saucedemo.com/");

            // Maksimalkan Browser
            driver.manage().window().maximize();

            // Delay 3 Detik
            delay();

            // Menjalankan Test Case
            testLogin();
            testAddToCart();
            testRemoveItem();
            testCheckout();
            testCheckoutNegative();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            // Menutup Browser
            driver.quit();

            // Simpan Excel
            saveExcel();
        }
    }

    // =========================
    // DELAY 3 DETIK
    // =========================
    public static void delay() {

        try {
            Thread.sleep(3000); // Delay 3 detik
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // TEST LOGIN
    // =========================
    public static void testLogin() {

        String testCaseId = "SIT-001";
        String testName = "Login Test";

        try {

            // Input Username
            driver.findElement(By.id("user-name")).sendKeys(username);
            delay();

            // Input Password
            driver.findElement(By.id("password")).sendKeys(password);
            delay();

            // Klik Login
            driver.findElement(By.id("login-button")).click();
            delay();

            // Validasi Login
            boolean isLoginSuccess =
                    driver.getCurrentUrl().contains("inventory");

            if (isLoginSuccess) {
                System.out.println("LOGIN SUCCESS");
                writeToExcel(testCaseId, testName, "SUCCESS");
            } else {
                System.out.println("LOGIN FAILURE");
                writeToExcel(testCaseId, testName, "FAIL");
            }

        } catch (Exception e) {
            System.out.println("LOGIN FAILURE");
            writeToExcel(testCaseId, testName, "FAIL");
        }
    }

    // =========================
    // TEST ADD TO CART
    // =========================
    public static void testAddToCart() {

        String testCaseId = "SIT-002";
        String testName = "Add To Cart";

        try {

            // Klik Add To Cart
            driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
            delay();

            // Ambil Badge Cart
            String cartBadge =
                    driver.findElement(By.className("shopping_cart_badge")).getText();

            // Validasi Add To Cart
            if (cartBadge.equals("1")) {
                System.out.println("ADD TO CART SUCCESS");
                writeToExcel(testCaseId, testName, "SUCCESS");
            } else {
                System.out.println("ADD TO CART FAILURE");
                writeToExcel(testCaseId, testName, "FAIL");
            }

        } catch (Exception e) {
            System.out.println("ADD TO CART FAILURE");
            writeToExcel(testCaseId, testName, "FAIL");
        }
    }

    // =========================
    // TEST REMOVE ITEM
    // =========================
    public static void testRemoveItem() {

        String testCaseId = "SIT-003";
        String testName = "Remove Item";

        try {

            // Klik Remove Item
            driver.findElement(By.id("remove-sauce-labs-backpack")).click();
            delay();

            // Validasi Cart Kosong
            boolean cartEmpty =
                    driver.findElements(By.className("shopping_cart_badge")).isEmpty();

            if (cartEmpty) {
                System.out.println("REMOVE ITEM SUCCESS");
                writeToExcel(testCaseId, testName, "SUCCESS");
            } else {
                System.out.println("REMOVE ITEM FAILURE");
                writeToExcel(testCaseId, testName, "FAIL");
            }

        } catch (Exception e) {
            System.out.println("REMOVE ITEM FAILURE");
            writeToExcel(testCaseId, testName, "FAIL");
        }
    }

    // =========================
    // TEST CHECKOUT POSITIVE
    // =========================
    public static void testCheckout() {

        String testCaseId = "SIT-004";
        String testName = "Checkout Positive";

        try {

            // Add Item
            driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
            delay();

            // Klik Cart
            driver.findElement(By.className("shopping_cart_link")).click();
            delay();

            // Klik Checkout
            driver.findElement(By.id("checkout")).click();
            delay();

            // Input First Name (Username)
            driver.findElement(By.id("first-name")).sendKeys(username);
            delay();

            // Input Last Name (Password)
            driver.findElement(By.id("last-name")).sendKeys(password);
            delay();

            // Input Zip Random
            driver.findElement(By.id("postal-code")).sendKeys(generateZip());
            delay();

            // Klik Continue
            driver.findElement(By.id("continue")).click();
            delay();

            // Klik Finish
            driver.findElement(By.id("finish")).click();
            delay();

            // Validasi Checkout
            boolean success =
                    driver.findElement(By.className("complete-header"))
                            .getText()
                            .contains("Thank you");

            if (success) {
                System.out.println("CHECKOUT SUCCESS");
                writeToExcel(testCaseId, testName, "SUCCESS");
            } else {
                System.out.println("CHECKOUT FAILURE");
                writeToExcel(testCaseId, testName, "FAIL");
            }

        } catch (Exception e) {
            System.out.println("CHECKOUT FAILURE");
            writeToExcel(testCaseId, testName, "FAIL");
        }
    }

    // =========================
    // TEST CHECKOUT NEGATIVE
    // =========================
    public static void testCheckoutNegative() {

        String testCaseId = "SIT-005";
        String testName = "Checkout Negative";

        try {

            // Kembali ke Inventory
            driver.get("https://www.saucedemo.com/inventory.html");
            delay();

            // Add Item
            driver.findElement(By.id("add-to-cart-sauce-labs-bike-light")).click();
            delay();

            // Klik Cart
            driver.findElement(By.className("shopping_cart_link")).click();
            delay();

            // Klik Checkout
            driver.findElement(By.id("checkout")).click();
            delay();

            // Klik Continue tanpa isi form
            driver.findElement(By.id("continue")).click();
            delay();

            // Validasi Error
            boolean error =
                    driver.findElement(By.className("error-message-container"))
                            .isDisplayed();

            if (error) {
                System.out.println("CHECKOUT NEGATIVE SUCCESS");
                writeToExcel(testCaseId, testName, "SUCCESS");
            } else {
                System.out.println("CHECKOUT NEGATIVE FAILURE");
                writeToExcel(testCaseId, testName, "FAIL");
            }

        } catch (Exception e) {
            System.out.println("CHECKOUT NEGATIVE FAILURE");
            writeToExcel(testCaseId, testName, "FAIL");
        }
    }

    // =========================
    // GENERATE ZIP RANDOM
    // =========================
    public static String generateZip() {

        Random random = new Random();
        int zip = 10000 + random.nextInt(90000);

        return String.valueOf(zip);
    }

    // =========================
    // CREATE HEADER EXCEL
    // =========================
    public static void createHeader() {

        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("Test Case ID");
        header.createCell(1).setCellValue("Test Name");
        header.createCell(2).setCellValue("Result");
        header.createCell(3).setCellValue("Timestamp");
    }

    // =========================
    // WRITE KE EXCEL
    // =========================
    public static void writeToExcel(String id, String name, String result) {

        Row row = sheet.createRow(rowNum++);

        row.createCell(0).setCellValue(id);
        row.createCell(1).setCellValue(name);
        row.createCell(2).setCellValue(result);
        row.createCell(3).setCellValue(getTimestamp());
    }

    // =========================
    // TIMESTAMP
    // =========================
    public static String getTimestamp() {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return LocalDateTime.now().format(formatter);
    }

    // =========================
    // SAVE EXCEL
    // =========================
    public static void saveExcel() {

        try {

            FileOutputStream file =
                    new FileOutputStream("SIT_Report.xlsx");

            workbook.write(file);

            workbook.close();
            file.close();

            System.out.println("SIT Report Generated");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}