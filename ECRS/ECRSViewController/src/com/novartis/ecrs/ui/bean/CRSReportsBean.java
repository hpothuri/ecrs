package com.novartis.ecrs.ui.bean;


import com.novartis.ecrs.ui.utility.ADFUtils;
import com.novartis.ecrs.ui.utility.ExcelExportUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

import oracle.adf.model.binding.DCIteratorBinding;

import oracle.javatools.resourcebundle.BundleFactory;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;

import oracle.security.crypto.util.InvalidFormatException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;


public class CRSReportsBean {
    public CRSReportsBean() {
        super();
    }

    ExcelExportUtils excelUtils = new ExcelExportUtils();
    
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
            ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                                cell.getColumnIndex(), true,CellStyle.ALIGN_CENTER);
            sheet.addMergedRegion(new CellRangeAddress(count, count, 2, 4));
            count++;

            // - Number of Compound CRSs
            org.apache.poi.ss.usermodel.Row row1 = sheet.createRow(count);
            Cell cell1 = row1.createCell((short)2);
            cell1.setCellValue("Number of Compound CRSs");
            ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                                cell1.getColumnIndex(), false,CellStyle.ALIGN_CENTER);
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
            ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                                cell2.getColumnIndex(), false,CellStyle.ALIGN_CENTER);
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
                    sheet.setColumnWidth(sheet.getRow(count).getCell(2).getColumnIndex(),
                                         12000);
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
            ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                                cell3.getColumnIndex(), false,CellStyle.ALIGN_CENTER);
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
            //write image to sheet
            ExcelExportUtils.writeImageTOExcel(sheet,getImageInpStream());
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
     * @param facesContext
     * @param outputStream
     * @throws IOException
     */
    public void downloadCountPerMedDRAReport(FacesContext facesContext,
                                             OutputStream outputStream) throws IOException {
        // Add event code here...
        //  _logger.info("Start of CRSReportsBean:onAdminReportItmes()");
        Workbook workbook = null;
        InputStream excelInputStream = getExcelInpStream();
        try {
            //create sheet
            DCIteratorBinding iter =
                ADFUtils.findIterator("CRSCountPerMeddraReportVOIterator");
            RowSetIterator rowSet = null;
            int rowStartIndex = 8;
            int cellStartIndex = 0;
            String emptyValReplace = null;
            String dateCellFormat = "M/dd/yyyy";
            if (iter != null) {
                iter.setRangeSize(-1);
                rowSet = iter.getRowSetIterator();
            }
            workbook = WorkbookFactory.create(excelInputStream);
            LinkedHashMap columnMap = new LinkedHashMap();
            ResourceBundle rsBundle =
                BundleFactory.getBundle("com.novartis.ecrs.model.ECRSModelBundle");
            //Here Key will be ViewObject Attribute
            columnMap.put("MeddraTerm",
                          rsBundle.getString("MEDDRA_COMPONENT"));
            columnMap.put("MeddraCompDefn", rsBundle.getString("LEVEL"));
            columnMap.put("NumberOfCrs", rsBundle.getString("NUMBER_OF_CRSS"));

            workbook.setMissingCellPolicy(org.apache.poi.ss.usermodel.Row.CREATE_NULL_AS_BLANK);
            Sheet sheet = workbook.getSheetAt(0);
            ExcelExportUtils.writeExcelSheet(sheet, rowSet, rowStartIndex,
                                             cellStartIndex, columnMap, null,
                                             dateCellFormat, emptyValReplace,getImageInpStream());
            //write image to sheet
            //ExcelExportUtils.writeImageTOExcel(sheet,getImageInpStream());
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

    }

    /**
     * @param facesContext
     * @param outputStream
     * @throws IOException
     */
    public void downloadCurrPendingCRSReport(FacesContext facesContext,
                                             OutputStream outputStream) throws IOException {
        // Add event code here...
        //  _logger.info("Start of CRSReportsBean:onAdminReportItmes()");
        Workbook workbook = null;
        InputStream excelInputStream = getExcelInpStream();
        try {
            //create sheet
            DCIteratorBinding iter =
                ADFUtils.findIterator("CRSCurrentPendingCRSReportIterator");
            RowSetIterator rowSet = null;
            int rowStartIndex = 8;
            int cellStartIndex = 0;
            String emptyValReplace = null;
            String dateCellFormat = "M/dd/yyyy";
            if (iter != null) {
                iter.setRangeSize(-1);
                rowSet = iter.getRowSetIterator();
            }
            workbook = WorkbookFactory.create(excelInputStream);
            LinkedHashMap columnMap = new LinkedHashMap();
            ResourceBundle rsBundle =
                BundleFactory.getBundle("com.novartis.ecrs.model.ECRSModelBundle");
            //Here Key will be ViewObject Attribute
            columnMap.put("CrsName", rsBundle.getString("CRS_NAME"));
            columnMap.put("CrsState", rsBundle.getString("CRS_STATUS"));
            columnMap.put("MeddraVers", rsBundle.getString("MEDDRA_VERS"));
            workbook.setMissingCellPolicy(org.apache.poi.ss.usermodel.Row.CREATE_NULL_AS_BLANK);
            Sheet sheet = workbook.getSheetAt(0);

            ExcelExportUtils.writeExcelSheet(sheet, rowSet, rowStartIndex,
                                             cellStartIndex, columnMap, null,
                                             dateCellFormat, emptyValReplace,getImageInpStream());
            //write image to sheet
            //ExcelExportUtils.writeImageTOExcel(sheet,getImageInpStream());
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

    }

    /**
     * @param facesContext
     * @param outputStream
     * @throws IOException
     */
    public void downloadMedDRACompReport(FacesContext facesContext,
                                         OutputStream outputStream) throws IOException {
        // Add event code here...
        //  _logger.info("Start of CRSReportsBean:onAdminReportItmes()");
        Workbook workbook = null;
        InputStream excelInputStream = getExcelInpStream();
        try {
            //create sheet
            DCIteratorBinding iter =
                ADFUtils.findIterator("MedDRAComponentsReportIterator");
            RowSetIterator rowSet = null;
            int rowStartIndex = 8;
            int cellStartIndex = 0;
            String emptyValReplace = null;
            String dateCellFormat = "M/dd/yyyy";
            if (iter != null) {
                iter.setRangeSize(-1);
                rowSet = iter.getRowSetIterator();
            }
            workbook = WorkbookFactory.create(excelInputStream);
            LinkedHashMap columnMap = new LinkedHashMap();
            ResourceBundle rsBundle =
                BundleFactory.getBundle("com.novartis.ecrs.model.ECRSModelBundle");
            //Here Key will be ViewObject Attribute
            columnMap.put("MeddraTerm", rsBundle.getString("DEFINITIONS"));
            columnMap.put("MeddraExtension", rsBundle.getString("LEVEL"));
            columnMap.put("SafetyTopicOfInterest",
                          rsBundle.getString("SAFETY_TOPIC"));
            columnMap.put("CrsName", rsBundle.getString("CRS_NAME"));
            columnMap.put("RiskPurposeList", rsBundle.getString("PURPOSE"));
            columnMap.put("SocTerm", rsBundle.getString("MQ_GROUP_OR_SOC"));
            workbook.setMissingCellPolicy(org.apache.poi.ss.usermodel.Row.CREATE_NULL_AS_BLANK);
            Sheet sheet = workbook.getSheetAt(0);

            ExcelExportUtils.writeExcelSheet(sheet, rowSet, rowStartIndex,
                                             cellStartIndex, columnMap, null,
                                             dateCellFormat, emptyValReplace,getImageInpStream());
            //write image to sheet
            //ExcelExportUtils.writeImageTOExcel(sheet,getImageInpStream());
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

    }

    /**
     * @param facesContext
     * @param outputStream
     * @throws IOException
     */
    public void downloadMedDRACompStandardReport(FacesContext facesContext,
                                                 OutputStream outputStream) throws IOException {
        // Add event code here...
        //  _logger.info("Start of CRSReportsBean:onAdminReportItmes()");
        Workbook workbook = null;
        InputStream excelInputStream = getExcelInpStream();
        try {
            //create sheet
            DCIteratorBinding iter =
                ADFUtils.findIterator("MedDRAStandardReportIterator");
            RowSetIterator rowSet = null;
            int rowStartIndex = 8;
            int cellStartIndex = 0;
            String emptyValReplace = null;
            String dateCellFormat = "M/dd/yyyy";
            if (iter != null) {
                iter.setRangeSize(-1);
                rowSet = iter.getRowSetIterator();
            }
            workbook = WorkbookFactory.create(excelInputStream);
            LinkedHashMap columnMap = new LinkedHashMap();
            ResourceBundle rsBundle =
                BundleFactory.getBundle("com.novartis.ecrs.model.ECRSModelBundle");
            //Here Key will be ViewObject Attribute
            columnMap.put("DefinitionType",
                          rsBundle.getString("DEFINITIONS_TYPE"));
            columnMap.put("Percentofuse", rsBundle.getString("_OF_USE"));

            workbook.setMissingCellPolicy(org.apache.poi.ss.usermodel.Row.CREATE_NULL_AS_BLANK);
            Sheet sheet = workbook.getSheetAt(0);

            ExcelExportUtils.writeExcelSheet(sheet, rowSet, rowStartIndex,
                                             cellStartIndex, columnMap, null,
                                             dateCellFormat, emptyValReplace,getImageInpStream());
            //write image to sheet
            //ExcelExportUtils.writeImageTOExcel(sheet,getImageInpStream());
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

    }

    /**
     * @param facesContext
     * @param outputStream
     * @throws IOException
     */
    public void downloadRetiredNMQsReport(FacesContext facesContext,
                                          OutputStream outputStream) throws IOException {
        // Add event code here...
        //  _logger.info("Start of CRSReportsBean:onAdminReportItmes()");
        Workbook workbook = null;
        InputStream excelInputStream = getExcelInpStream();
        try {
            //create sheet
            DCIteratorBinding iter =
                ADFUtils.findIterator("RetiredNmqCmqPrevUsedReportIterator");
            RowSetIterator rowSet = null;
            int rowStartIndex = 8;
            int cellStartIndex = 0;
            String emptyValReplace = null;
            String dateCellFormat = "M/dd/yyyy";
            if (iter != null) {
                iter.setRangeSize(-1);
                rowSet = iter.getRowSetIterator();
            }
            workbook = WorkbookFactory.create(excelInputStream);
            LinkedHashMap columnMap = new LinkedHashMap();
            ResourceBundle rsBundle =
                BundleFactory.getBundle("com.novartis.ecrs.model.ECRSModelBundle");
            //Here Key will be ViewObject Attribute
            columnMap.put("MeddraTerm", rsBundle.getString("DEFINITIONS"));
            columnMap.put("CrsEffectiveDt",
                          rsBundle.getString("com.novartis.ecrs.model.view.report.RetiredNmqCmqprevUsedReport.CrsEffectiveDt_LABEL"));
            columnMap.put("CrsName", rsBundle.getString("CRS_NAME"));
            workbook.setMissingCellPolicy(org.apache.poi.ss.usermodel.Row.CREATE_NULL_AS_BLANK);
            Sheet sheet = workbook.getSheetAt(0);

            ExcelExportUtils.writeExcelSheet(sheet, rowSet, rowStartIndex,
                                             cellStartIndex, columnMap, null,
                                             dateCellFormat, emptyValReplace,
                                             getImageInpStream());
            //write image to sheet
            //ExcelExportUtils.writeImageTOExcel(sheet,getImageInpStream());
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
    }

    /**
     * @param facesContext
     * @param outputStream
     * @throws IOException
     */
    public void downloadRiskDefSafetyTopicReport(FacesContext facesContext,
                                                 OutputStream outputStream) throws IOException {
        // Add event code here...
        //  _logger.info("Start of CRSReportsBean:onAdminReportItmes()");
        Workbook workbook = null;
        InputStream excelInputStream = getExcelInpStream();
        try {
            //create sheet
            DCIteratorBinding iter =
                ADFUtils.findIterator("RiskDefSafetyTopicReportIterator");
            RowSetIterator rowSet = null;
            int rowStartIndex = 8;
            int cellStartIndex = 0;
            String emptyValReplace = null;
            String dateCellFormat = "M/dd/yyyy";
            if (iter != null) {
                iter.setRangeSize(-1);
                rowSet = iter.getRowSetIterator();
            }
            workbook = WorkbookFactory.create(excelInputStream);
            LinkedHashMap columnMap = new LinkedHashMap();
            ResourceBundle rsBundle =
                BundleFactory.getBundle("com.novartis.ecrs.model.ECRSModelBundle");
            //Here Key will be ViewObject Attribute
            columnMap.put("CrsName", rsBundle.getString("CRS_NAME"));
            columnMap.put("SafetyTopicOfInterest",
                          rsBundle.getString("SAFETY_TOPIC"));
            columnMap.put("MeddraTermCount", rsBundle.getString("MEDDRA_TERM"));
            columnMap.put("SmqCount", rsBundle.getString("SMQ"));
            columnMap.put("NmqCount", rsBundle.getString("NMQ"));
            columnMap.put("CmqCount", rsBundle.getString("CMQ"));
            columnMap.put("AdrCount", rsBundle.getString("ADR_CDS"));
            workbook.setMissingCellPolicy(org.apache.poi.ss.usermodel.Row.CREATE_NULL_AS_BLANK);
            Sheet sheet = workbook.getSheetAt(0);
            
            ExcelExportUtils.writeExcelSheet(sheet, rowSet, rowStartIndex,
                                             cellStartIndex, columnMap, null,
                                             dateCellFormat, emptyValReplace,getImageInpStream());
            //write image to sheet
            //ExcelExportUtils.writeImageTOExcel(sheet,getImageInpStream());
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
    }
    
    /**
     * @return InputStream
     */
    public InputStream getExcelInpStream() {
        return excelUtils.getExcelInpStream();
    }
    
    /**
     * @return InputStream
     */
    public InputStream getImageInpStream() {
        return excelUtils.getImageInpStream();
    }
}
