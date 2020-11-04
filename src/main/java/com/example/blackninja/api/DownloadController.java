package com.example.blackninja.api;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/download")
public class DownloadController {

    @GetMapping("/prakhar")
    public ResponseEntity getContact() throws IOException {
        File file = new ClassPathResource("static/prakhar-contanct-details.jpg").getFile();
        byte[] fileContent = FileUtils.readFileToByteArray(file);
        HttpHeaders headers = new HttpHeaders();
        List<String> exposeHeaders = new ArrayList<String>();
        exposeHeaders.add("Content-Disposition");
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.setContentDispositionFormData(file.getName(), file.getName());
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        headers.setAccessControlExposeHeaders(exposeHeaders);
        return new ResponseEntity(fileContent, headers, HttpStatus.OK);
    }
}
