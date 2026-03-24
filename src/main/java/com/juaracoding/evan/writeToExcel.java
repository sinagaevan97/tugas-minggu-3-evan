package com.juaracoding.evan;

public static void writeToExcel(String result) {

    try {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("SIT");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Test Case");
        header.createCell(1).setCellValue("Result");

        Row row = sheet.createRow(1);
        row.createCell(0).setCellValue("Add To Cart Test");
        row.createCell(1).setCellValue(result);

        FileOutputStream file = new FileOutputStream("SIT.xlsx");
        workbook.write(file);

        workbook.close();
        file.close();

        System.out.println("Spreadsheet Created");

    } catch (IOException e) {
        e.printStackTrace();
    }
}