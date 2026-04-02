package com.priya.Drive_BE.services;


import com.priya.Drive_BE.entity.FileEntity;
import com.priya.Drive_BE.repo.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileServiceStorage {

    @Value("${file.uploads-dir}")
    private String uploadDir;

    private final FileRepository fileRepository;

    public FileServiceStorage(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public String saveFile(MultipartFile file, Long parentFolderId) throws IOException {

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        Path filePath = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(fileName);
        fileEntity.setPath(filePath.toString());
        fileEntity.setSize(file.getSize());
        fileEntity.setType("file");
        fileEntity.setParentHolderId(parentFolderId);
        fileEntity.setCreatedAt(LocalDateTime.now());

        fileRepository.save(fileEntity);

        return "File uploaded successfully";


        //HOMEWORK --->
        //1.PLACE UPLOAD LOGIC IN TRY CATCH AND HANDLE EXCEPTION IT OCCURS
    }



    public List<FileEntity> getFilesInFolder(Long parentFolderId) {

        if (parentFolderId == null) {
            return fileRepository.findAll()
                    .stream()
                    .filter(f -> f.getParentHolderId() == null)
                    .collect(Collectors.toList());
        } else {
            return fileRepository.findAll()
                    .stream()
                    .filter(f -> parentFolderId.equals(f.getParentHolderId()))
                    .collect(Collectors.toList());
        }
    }


    public FileEntity getFileById(Long id) {
        return fileRepository.findById(id).orElseThrow(()->new RuntimeException("file not found"));
    }


    public void deleteFileById(Long id) {
        fileRepository.deleteById(id);
    }




}