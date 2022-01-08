package com.example.TaxExercise.Controllers;

import com.example.TaxExercise.Models.EmployeeDetails;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.http.HttpHeaders;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;
import java.lang.String;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class EmployeeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/upload-csv-file")
    public void uploadCSVFile(HttpServletResponse response,@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            model.addAttribute("status", false);
        } else {

            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                char[] buffer = new char[8192];
                StringBuilder faultyFileString = new StringBuilder();
                int charsRead;
                while ((charsRead = reader.read(buffer, 0, buffer.length)) > 0) {
                    faultyFileString.append(buffer, 0, charsRead);
                }
                String modifiedFileString = faultyFileString.toString().substring(3);
                Reader modifiedFileReader = new StringReader(modifiedFileString);

                CsvToBean<EmployeeDetails> csvToBean = new CsvToBeanBuilder(modifiedFileReader)
                        .withType(EmployeeDetails.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                List<EmployeeDetails> employeeDetails = new ArrayList<EmployeeDetails>(csvToBean.parse());

                for(int i = 0;i < employeeDetails.size();i++){
                    employeeDetails.get(i).GeneratePay();
                }

                model.addAttribute("status", true);

                String filename = "Employees.csv";

                response.setContentType("text/csv");
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"");

                //create a csv writer
                StatefulBeanToCsv<EmployeeDetails> writer = new StatefulBeanToCsvBuilder<EmployeeDetails>(response.getWriter())
                        .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                        .withOrderedResults(false)
                        .build();

                //write all users to csv file
                writer.write(employeeDetails);

            } catch (Exception ex) {
                model.addAttribute("message", "An error occurred while processing the CSV file.");
                model.addAttribute("status", false);
            }
        }





    }
}
