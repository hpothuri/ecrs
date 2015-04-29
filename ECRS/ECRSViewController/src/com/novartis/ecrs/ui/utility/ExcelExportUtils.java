package com.novartis.ecrs.ui.utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;


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
    
    public static Sheet writeExcelSheet(Sheet sheet, RowSetIterator rowSet,
                                  int rowStartIndex, int cellStartIndex,
                                  LinkedHashMap columnMap, String tableHeader,
                                  String dateCellFormat,
                                  String emptyValReplace,InputStream imageInpStream) throws IOException {
        int rowIndex = rowStartIndex;
        org.apache.poi.ss.usermodel.Row row = null;
        //Write Table Header
           if(tableHeader!=null){
               row = sheet.getRow(rowIndex) != null ? sheet.getRow(rowIndex) : sheet.createRow(rowIndex) ;
               writeTableHeader(sheet, row, rowIndex,cellStartIndex, columnMap, tableHeader);
               rowIndex++;
           }           
           //Write HeaderRow       
           row = sheet.getRow(rowIndex) != null ? sheet.getRow(rowIndex) : sheet.createRow(rowIndex) ;
           writeHeaderRow(sheet, row, rowIndex,cellStartIndex, columnMap,tableHeader);
           rowIndex++;
           
           //Write Cell        
           for(int rowCounter=0; rowCounter< rowSet.getAllRowsInRange().length; rowCounter++){                 
               row = sheet.getRow(rowIndex) != null ? sheet.getRow(rowIndex) : sheet.createRow(rowIndex) ;                     
               writeData(sheet, row, rowIndex,cellStartIndex, columnMap, rowSet, rowCounter,tableHeader,dateCellFormat,emptyValReplace); 
               rowIndex++;          
           }
           writeImageTOExcel(sheet,imageInpStream);
          return sheet;
       }
      
       private static void writeTableHeader(Sheet sheet, org.apache.poi.ss.usermodel.Row row,
                                  int rowIndex, int cellStartIndex, LinkedHashMap columnMap, String tableHeader){
           Cell cell = row.getCell(cellStartIndex) != null ? row.getCell(cellStartIndex) : row.createCell(cellStartIndex) ; 
           cell.setCellValue(tableHeader);
           int cellEndIndex=cellStartIndex+columnMap.size()-1;
           sheet.addMergedRegion(new CellRangeAddress(
                   rowIndex, //first row (0-based)
                   rowIndex, //last row  (0-based)
                   cellStartIndex, //first column (0-based)
                   cellEndIndex  //last column  (0-based)
           ));
           setTableHeaderCellStyle(sheet, rowIndex, cellStartIndex);          
       }
       
       private static void writeHeaderRow(Sheet sheet, org.apache.poi.ss.usermodel.Row row,
                                  int rowIndex, int cellStartIndex, LinkedHashMap columnMap, String tableHeader){
           int cellIndex=cellStartIndex;
           Iterator entries = columnMap.entrySet().iterator();
           while (entries.hasNext()) {
               Map.Entry entry = (Map.Entry) entries.next();          
               String value = (String)entry.getValue();          
               org.apache.poi.ss.usermodel.Cell cell = null;
               cell = row.getCell(cellIndex) != null ? row.getCell(cellIndex) : row.createCell(cellIndex) ; 
               cell.setCellValue(value);
               setHeaderCellStyle(sheet, rowIndex, cellIndex);           
               sheet.setColumnWidth(cellIndex, 4000);
               cellIndex++;
           }   
       }

    private static void writeData(Sheet sheet,
                                  org.apache.poi.ss.usermodel.Row row,
                                  int rowIndex, int cellStartIndex,
                                  LinkedHashMap columnMap,
                                  RowSetIterator rowSet, int rowCounter,
                                  String tableHeader, String dateFormat,
                                  String emptyValReplace) {
        int cellIndex=cellStartIndex;           
           Iterator entries = columnMap.entrySet().iterator();
           while (entries.hasNext()) {
               Map.Entry entry = (Map.Entry) entries.next();
               Row currentRow=rowSet.getRowAtRangeIndex(rowCounter);  
               org.apache.poi.ss.usermodel.Cell cell = null;
               cell = row.getCell(cellIndex) != null ? row.getCell(cellIndex) : row.createCell(cellIndex) ; 
               String attribute = (String)entry.getKey();
               if (currentRow.getAttribute(attribute) == null){
                   if(emptyValReplace!=null)
                        cell.setCellValue(emptyValReplace);
                   else
                    cell.setCellValue("");
               }
               else {
                   if (currentRow.getAttribute(attribute) instanceof Number)
                           cell.setCellValue(Long.parseLong(currentRow.getAttribute(attribute).toString()));
                   else if((currentRow.getAttribute(attribute) instanceof Date)){ 
                               SimpleDateFormat sdf  = new SimpleDateFormat(dateFormat);
                               String date = sdf.format((Date)currentRow.getAttribute(attribute));
                               cell.setCellValue(date);                  
                        }                    
                   else{
                       //Custom Code to write Status based on Status Code for LastStatusCol                                            
                         cell.setCellValue(currentRow.getAttribute(attribute).toString());  
                   }   
               }
               if ("RiskPurposeSpFlag".equals(attribute) ||
                "RiskPurposeDsFlag".equals(attribute) ||
                "RiskPurposeRmFlag".equals(attribute) ||
                "RiskPurposePsFlag".equals(attribute) ||
                "RiskPurposeIbFlag".equals(attribute) ||
                "RiskPurposeCdFlag".equals(attribute) ||
                "RiskPurposeOsFlag".equals(attribute) ||
                "RiskPurposeMiFlag".equals(attribute) ||
                "RiskPurposeErFlag".equals(attribute))
                sheet.setColumnWidth(cellIndex, 1000);
            else
                sheet.setColumnWidth(cellIndex, 6000);
               
               setDataCellStyle(sheet, rowIndex, cellIndex);
               cellIndex++; 
                
           }       
       }
       
       private static void setTableHeaderCellStyle(Sheet sheet, int rowIndex, int cellIndex) {
           org.apache.poi.ss.usermodel.Row row = sheet.getRow((short)rowIndex);
           Workbook wb =sheet.getWorkbook();
           Font font = wb.createFont();    
           font.setColor(Font.BOLDWEIGHT_BOLD);
           font.setBoldweight(Font.BOLDWEIGHT_BOLD);
           CellStyle cellStyle = wb.createCellStyle();
           cellStyle.setFont(font);
           Cell cell = row.getCell((short)cellIndex);       
           cellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
           cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
           cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
           cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
           cell.setCellStyle(cellStyle);
       }  
       
       
       private static void setHeaderCellStyle(Sheet sheet, int rowIndex, int cellIndex) {
           org.apache.poi.ss.usermodel.Row row = sheet.getRow((short)rowIndex);
           Workbook wb =sheet.getWorkbook();
           Font font = wb.createFont();    
           font.setColor(Font.BOLDWEIGHT_BOLD);
           font.setBoldweight(Font.BOLDWEIGHT_BOLD);
           CellStyle cellStyle = wb.createCellStyle();
           cellStyle.setFont(font);
           Cell cell = row.getCell((short)cellIndex);    
           cellStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());     
           cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
           cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
           cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
           cell.setCellStyle(cellStyle);
       }  
       
       private static void setDataCellStyle(Sheet sheet, int rowIndex, int cellIndex){
           org.apache.poi.ss.usermodel.Row row = sheet.getRow((short)rowIndex);
           Workbook wb = sheet.getWorkbook();
           CellStyle cellStyle = wb.createCellStyle();
           Cell cell = row.getCell((short)cellIndex);
           cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
           cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
           cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
           cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
           cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
           cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
           cellStyle.setBorderRight(CellStyle.BORDER_THIN);
           cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
           cellStyle.setBorderTop(CellStyle.BORDER_THIN);
           cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
           cell.setCellStyle(cellStyle);
       }
       
    
    /**
     * This method to upload the file from local.
     * @param resourceName
     * @return
     */
    public static InputStream loadResourceAsStream(final String resourceName) {
        //_logger.info("Start of CRSReportsBean:loadResourceAsStream()");
        InputStream input = null;
        try {
            input = new FileInputStream(resourceName);
        } catch (FileNotFoundException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          null,
                                                                          "File not found in the provided location"));
            //  _logger.log(_logger.ERROR, "Exception occured in ManageDealsBean loadResourceAsStream() method", e);
            e.printStackTrace();
            //  _logger.severe("Exception message ManageDealsBean loadResourceAsStream()-->"+e.getMessage());
        }
        // _logger.info("End of CRSReportsBean:loadResourceAsStream()");
        return input;
    }

    /**
     * @param sheet
     * @throws IOException
     */
    public static void writeImageTOExcel(Sheet sheet,InputStream imageInputStream)throws IOException{
        byte[] bytes = IOUtils.toByteArray(imageInputStream);
        int pictureIdx = sheet.getWorkbook().addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        imageInputStream.close();
        CreationHelper helper = sheet.getWorkbook().getCreationHelper();
        Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setCol1(0);
        anchor.setRow1(0);
        Picture pict = drawing.createPicture(anchor, pictureIdx);
        pict.resize();
    }
    
    /**
     * @return InputStream
     */
    public InputStream getExcelInpStream() {
        InputStream inputStreamOfExcel =
             this.getClass().getClassLoader().getResourceAsStream("EcrsReports.xls");
//            ExcelExportUtils.loadResourceAsStream("E:\\Ecrs_WS\\ecrs\\trunk\\ECRS\\ECRSViewController\\public_html\\exceltemplate\\EcrsReports.xls");
        return inputStreamOfExcel;
    }
    
    /**
     * @return InputStream
     */
    public InputStream getImageInpStream() {
        InputStream inputStreamOfExcel =
             this.getClass().getClassLoader().getResourceAsStream("crs.png");
//            ExcelExportUtils.loadResourceAsStream("E:\\Ecrs_WS\\ecrs\\trunk\\ECRS\\ECRSViewController\\public_html\\images\\crs.png");
        return inputStreamOfExcel;
    }
}
