package com.enotes.Enotes_INDUS.utils;

import org.apache.poi.ss.usermodel.*;


import com.enotes.Enotes_INDUS.dto.NotesDto;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


@Service
public class UserExportToExcelService {

        @Value("${file.upload.pathExcel}")
        private String uploadPath;
    public ByteArrayOutputStream exportToExcel(List<NotesDto> exportNotesDTO) {
        Workbook workbook =new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Notes");



//        row creation
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Title", "Description", "Category", "Created On", "Created BY","File Name"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowN=1;
        for(NotesDto note: exportNotesDTO ){
            Row row= sheet.createRow(rowN++);
            row.createCell(0).setCellValue(note.getId());
            row.createCell(1).setCellValue(note.getTitle());
            row.createCell(2).setCellValue(note.getDescription());
            row.createCell(3).setCellValue(note.getCategory()!=null ? note.getCategory().getName():"");
            row.createCell(4).setCellValue(note.getCreatedOn());
            row.createCell(5).setCellValue(1);
            row.createCell(6).setCellValue(note.getFileDetails()!=null ? note.getFileDetails().getDisplayFileName():"");

        }
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

//            String filePath = Paths.get(uploadPath, "notes_export.xlsx").toString();
//            FileOutputStream fileOut = new FileOutputStream(filePath);
//            workbook.write(fileOut);
//            fileOut.close();
//            workbook.close();
//            System.out.println("Excel exported successfully at: " + filePath);
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

                // Ensure upload directory exists
                Files.createDirectories(Paths.get(uploadPath));

                String filePath = Paths.get(uploadPath, "notes_export.xlsx").toString();

                // Write workbook both to file and memory
                try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                    workbook.write(fileOut);  // save to file
                    workbook.write(out);      // save to memory stream
                }

                workbook.close();
                System.out.println("Excel exported successfully at: " + filePath);

                return out;  // return stream for possible download use

            } catch (IOException e) {
            throw new RuntimeException("Failed to export Excel: " + e.getMessage(), e);
        }


    }
}
