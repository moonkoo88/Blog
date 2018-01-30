package com.mkcheong.service;

import com.mkcheong.domain.ImageFile;
import com.mkcheong.persistence.ImageFileRepository;
import com.mkcheong.util.UploadFileUtils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Service
@Log
public class ImageService {

    private final Path rootLocation;

    @Autowired
    public ImageService(String uploadPath) {
        log.info("PATH :: " + uploadPath);
        this.rootLocation = Paths.get(uploadPath);
    }

    @Autowired
    ImageFileRepository repo;

    public Stream<Long> loadAll() {
        List<ImageFile> files = repo.findAll();
        return files.stream().map(file -> file.getId());
    }

    public ImageFile load(Long fileId) {
        return repo.findOneById(fileId);
    }

    public Resource loadAsResource(String fileName) throws Exception {
        try {
            if (fileName.toCharArray()[0] == '/') {
                fileName = fileName.substring(1);
            }

            Path file = loadPath(fileName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new Exception("Could not read file: " + fileName);
            }
        } catch (Exception e) {
            throw new Exception("Could not read file: " + fileName);
        }
    }

    private Path loadPath(String fileName) {
        return rootLocation.resolve(fileName);
    }

    public ImageFile store(MultipartFile file) throws Exception {
        try {
            if (file.isEmpty()) {
                throw new Exception("Failed to store empty file " + file.getOriginalFilename());
            }

            String saveFileName = UploadFileUtils.fileSave(rootLocation.toString(), file);

            if (saveFileName.toCharArray()[0] == '/') {
                saveFileName = saveFileName.substring(1);
            }

            Resource resource = loadAsResource(saveFileName);

            ImageFile saveFile = new ImageFile();
            saveFile.setSaveFileName(saveFileName);
            saveFile.setFileName(file.getOriginalFilename());
            saveFile.setContentType(file.getContentType());
            saveFile.setFilePath(rootLocation.toString().replace(File.separatorChar, '/') + File.separator + saveFileName);
            saveFile.setSize(resource.contentLength());
            saveFile = repo.save(saveFile);

            return saveFile;
        } catch (IOException e) {
            throw new Exception("Failed to store file " + file.getOriginalFilename(), e);
        }
    }
}
