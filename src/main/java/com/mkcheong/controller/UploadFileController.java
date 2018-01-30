package com.mkcheong.controller;

import com.mkcheong.domain.UploadFile;
import com.mkcheong.domain.WebBoard;
import com.mkcheong.persistence.UploadFileRepository;
import com.mkcheong.service.UploadFileService;
import com.mkcheong.util.UploadFileUtils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/file/*")
@Log
public class UploadFileController {

    @Autowired
    private UploadFileRepository fileRepo;

    @Autowired
    UploadFileService uploadFileService;


    @GetMapping("/{bno}")
    public ResponseEntity<List<UploadFile>> getFiles(
            @PathVariable("bno")Long bno){

        log.info("get All files..........................");

        WebBoard board = new WebBoard();
        board.setBno(bno);
        return new ResponseEntity<>(getListByBoard(bno), HttpStatus.OK );
    }

    @GetMapping("/down/{fileId}")
    public ResponseEntity<?> serveFile(@PathVariable long fileId) {
        try {
            UploadFile uploadedFile = uploadFileService.load(fileId);
            HttpHeaders headers = new HttpHeaders();

            Resource resource = uploadFileService.loadAsResource(uploadedFile.getSaveFileName());
            String fileName = uploadedFile.getFileName();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity.ok().headers(headers).body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Secured(value={"ROLE_BASIC","ROLE_MANAGER","ROLE_ADMIN"})
    @Transactional
    @DeleteMapping("/{bno}/{id}")
    public ResponseEntity<List<UploadFile>> remove(
            @PathVariable("bno")Long bno,
            @PathVariable("id")Long id) {

        log.info("delete file: "+ id);

        UploadFile deleteFile = fileRepo.findOneById(id);

        try {
            UploadFileUtils.fileDelete(deleteFile.getFilePath());
        } catch (IOException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

        fileRepo.deleteById(id);

        return  new ResponseEntity<>(getListByBoard(bno), HttpStatus.OK);

    }

    private List<UploadFile> getListByBoard(Long bno)throws RuntimeException{

        return fileRepo.getUploadFileOsfBoard(bno);

    }
}
