package com.juaracoding.evan;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddToCartTest {

    static WebDriver driver;
    static Workbook workbook = new XSSFWorkbook();
    static Sheet sheet = workbook.createSheet("SIT");
    static int rowNum = 1;

    public static void main(String[] args) {

        // Setup Driver
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        // Create Header Excel
        createHeader();

        try {

            // Open Website
            driver.get("https://www.saucedemo.com/");
            driver.manage().window().maximize();

            // Run Test Cases
            testLogin();
            testAddToCart();
            testRemoveItem();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            // Close Browser
            driver.quit();

            // Save Excel
            saveExcel();
        }
    }

    // =========================
    // TEST CASE 1 - LOGIN
    // =========================
    public static void testLogin() {

        String testCaseId = "SIT-001";
        String testName = "Login Test";

        try {

            driver.findElement(By.id("user-name")).sendKeys("standard_user");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();

            boolean isLoginSuccess =
                    driver.getCurrentUrl().contains("inventory");

            if (isLoginSuccess) {
                writeToExcel(testCaseId, testName, "SUCCESS");
            } else {
                writeToExcel(testCaseId, testName, "FAIL");
            }

        } catch (Exception e) {
            writeToExcel(testCaseId, testName, "FAIL");
        }
    }

    // =========================
    // TEST CASE 2 - ADD TO CART
    // =========================
    public static void testAddToCart() {

        String testCaseId = "SIT-002";
        String testName = "Add To Cart";

        try {

            driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();

            String cartBadge =
                    driver.findElement(By.className("shopping_cart_badge")).getText();

            if (cartBadge.equals("1")) {
                writeToExcel(testCaseId, testName, "SUCCESS");
            } else {
                writeToExcel(testCaseId, testName, "FAIL");
            }

        } catch (Exception e) {
            writeToExcel(testCaseId, testName, "FAIL");
        }
    }

    // =========================
    // TEST CASE 3 - REMOVE ITEM
    // =========================
    public static void testRemoveItem() {

        String testCaseId = "SIT-003";
        String testName = "Remove Item";

        try {

            driver.findElement(By.id("remove-sauce-labs-backpack")).click();

            boolean cartEmpty =
                    driver.findElements(By.className("shopping_cart_badge")).isEmpty();

            if (cartEmpty) {
                writeToExcel(testCaseId, testName, "SUCCESS");
            } else {
                writeToExcel(testCaseId, testName, "FAIL");
            }

        } catch (Exception e) {
            writeToExcel(testCaseId, testName, "FAIL");
        }
    }

    // =========================
    // CREATE HEADER
    // =========================
    public static void createHeader() {

        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("Test Case ID");
        header.createCell(1).setCellValue("Test Name");
        header.createCell(2).setCellValue("Result");
        header.createCell(3).setCellValue("Timestamp");
    }

    // =========================
    // WRITE TO EXCEL
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