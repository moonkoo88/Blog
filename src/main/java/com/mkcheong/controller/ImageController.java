package com.mkcheong.controller;

import com.mkcheong.domain.ImageFile;
import com.mkcheong.service.ImageService;

import com.mkcheong.util.MediaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageController {

    @Autowired
    ImageService imageService;

    @Secured(value={"ROLE_BASIC","ROLE_MANAGER","ROLE_ADMIN"})
    @PostMapping("/image")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            ImageFile uploadedFile = imageService.store(file);
            return ResponseEntity.ok().body("/image/" + uploadedFile.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/image/{fileId}")
    public ResponseEntity<?> serveFile(@PathVariable long fileId) {
        try {
            ImageFile uploadedFile = imageService.load(fileId);
            HttpHeaders headers = new HttpHeaders();

            Resource resource = imageService.loadAsResource(uploadedFile.getSaveFileName());
            String fileName = uploadedFile.getFileName();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");

            if (MediaUtils.containsImageMediaType(uploadedFile.getContentType())) {
                headers.setContentType(MediaType.valueOf(uploadedFile.getContentType()));
            } else {
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }

            return ResponseEntity.ok().headers(headers).body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}
