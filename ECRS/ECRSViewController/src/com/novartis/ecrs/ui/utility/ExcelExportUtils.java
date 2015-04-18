package com.novartis.ecrs.ui.utility;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelExportUtils {
    public ExcelExportUtils() {
        super();
    }
    

    public static void setHeaderCellStyle(Sheet sheet, int rowIndex,
                                           int cellIndex,
                                           boolean isAdminTitle) {
        org.apache.poi.ss.usermodel.Row row = sheet.getRow((short)rowIndex);
        Workbook wb = sheet.getWorkbook();
        Font font = wb.createFont();
        font.setColor(Font.BOLDWEIGHT_BOLD);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFont(font);
        Cell cell = row.getCell((short)cellIndex);
        if (isAdminTitle)
            cellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        else
            cellStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cell.setCellStyle(cellStyle);
    }
}
