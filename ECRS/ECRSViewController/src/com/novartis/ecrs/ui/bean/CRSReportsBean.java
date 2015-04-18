package com.novartis.ecrs.ui.bean;


import com.novartis.ecrs.ui.utility.ADFUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import oracle.adf.model.binding.DCIteratorBinding;

import oracle.jbo.Row;

import oracle.security.crypto.util.InvalidFormatException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;


public class CRSReportsBean {
    public CRSReportsBean() {
        super();
    }

    /**
     * @param facesContext
     * @param outputStream
     * @throws IOException
     */
    public void downloadAdminReport(FacesContext facesContext,
                                    OutputStream outputStream) throws IOException {
        // Add event code here...
        //  _logger.info("Start of CRSReportsBean:onAdminReportItmes()");
        Workbook workbook = null;
        InputStream excelInputStream = getExcelInpStream();
        try {
            //create sheet
            //            org.apache.poi.ss.usermodel.Row row = null;
            //            Cell cell = null;
            workbook = WorkbookFactory.create(excelInputStream);

            int count = 8;
            workbook.setMissingCellPolicy(org.apache.poi.ss.usermodel.Row.CREATE_NULL_AS_BLANK);
            Sheet sheet = workbook.getSheetAt(0);
            //write header - Admin Dashboard Report
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(count);
            Cell cell = row.createCell((short)2);
            cell.setCellValue("Admin Dashboard Report");
            setHeaderCellStyle(sheet, count, cell.getColumnIndex(),true);
            sheet.addMergedRegion(new CellRangeAddress(count, count, 2, 4));
            count++;

            // - Number of Compound CRSs
            org.apache.poi.ss.usermodel.Row row1 = sheet.createRow(count);
            Cell cell1 = row1.createCell((short)2);
            cell1.setCellValue("Number of Compound CRSs");
            setHeaderCellStyle(sheet, count, cell1.getColumnIndex(),false);
            sheet.addMergedRegion(new CellRangeAddress(count, count, 2, 4));
            count++;
            DCIteratorBinding compCrsIter =
                ADFUtils.findIterator("NumberOfCompoundCrsReportIterator");
            if (compCrsIter != null) {
                oracle.jbo.Row[] rows = compCrsIter.getAllRowsInRange();

                for (Row rw : rows) {
                    // org.apache.poi.ss.usermodel.Row row6 = sheet.createRow(count);
                    sheet.getRow(count).getCell(2).setCellValue(rw.getAttribute("Totalnumberofcrss") ==
                                                                null ? "" :
                                                                rw.getAttribute("Totalnumberofcrss").toString());
                    sheet.getRow(count).getCell(3).setCellValue(rw.getAttribute("Count1") ==
                                                                null ? "" :
                                                                rw.getAttribute("Count1").toString());
                    count++;
                }
            }

            //- Number of saftey topics (independent of CRSs)
            org.apache.poi.ss.usermodel.Row row2 = sheet.createRow(count);
            Cell cell2 = row2.createCell((short)2);
            cell2.setCellValue("Number of saftey topics (independent of CRSs)");
            setHeaderCellStyle(sheet, count, cell2.getColumnIndex(),false);
            sheet.addMergedRegion(new CellRangeAddress(count, count, 2, 4));
            count++;
            DCIteratorBinding safetyTopicIter =
                ADFUtils.findIterator("TotalNumOfSafetyTopicsReportIterator");
            if (safetyTopicIter != null) {
                oracle.jbo.Row[] rows = safetyTopicIter.getAllRowsInRange();
                for (Row rw : rows) {
                    sheet.getRow(count).getCell(2).setCellValue(rw.getAttribute("Totalnumberofsafetytopics") ==
                                                                null ? "" :
                                                                rw.getAttribute("Totalnumberofsafetytopics").toString());
                    sheet.getRow(count).getCell(3).setCellValue(rw.getAttribute("Totalsafetytopics") ==
                                                                null ? "" :
                                                                rw.getAttribute("Totalsafetytopics").toString());
                    count++;
                }
            }
            // -Number of ADRs (CDSs)
            org.apache.poi.ss.usermodel.Row row3 = sheet.createRow(count);
            Cell cell3 = row3.createCell((short)2);
            cell3.setCellValue("Number of ADRs (CDSs)");
            setHeaderCellStyle(sheet, count, cell3.getColumnIndex(),false);
            sheet.addMergedRegion(new CellRangeAddress(count, count, 2, 4));
            count++;
            DCIteratorBinding adrIter =
                ADFUtils.findIterator("TotalNumOfADRsReportIterator");
            if (adrIter != null) {
                oracle.jbo.Row[] rows = adrIter.getAllRowsInRange();
                for (Row rw : rows) {
                    sheet.getRow(count).getCell(2).setCellValue(rw.getAttribute("Totalnumberadrs") ==
                                                                null ? "" :
                                                                rw.getAttribute("Totalnumberadrs").toString());
                    sheet.getRow(count).getCell(3).setCellValue(rw.getAttribute("Totalnumofadrs") ==
                                                                null ? "" :
                                                                rw.getAttribute("Totalnumofadrs").toString());
                    count++;
                }
            }

        } catch (InvalidFormatException invalidFormatException) {
            invalidFormatException.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workbook.write(outputStream);
            excelInputStream.close();
            outputStream.close();
        }

        //_logger.info("End of CRSReportsBean:onAdminReportItmes()");
    }

    /**
     * @return InputStream
     */
    private InputStream getExcelInpStream() {
        InputStream inputStreamOfExcel =
            // this.getClass().getClassLoader().getResourceAsStream("EcrsReports.xls");
            loadResourceAsStream("E:\\Ecrs_WS\\ecrs\\trunk\\ECRS\\ECRSViewController\\public_html\\exceltemplate\\EcrsReports.xls");
        return inputStreamOfExcel;
    }

    /**
     * This method to upload the file from local.
     * @param resourceName
     * @return
     */
    public InputStream loadResourceAsStream(final String resourceName) {
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


    private static void setHeaderCellStyle(Sheet sheet, int rowIndex,
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
