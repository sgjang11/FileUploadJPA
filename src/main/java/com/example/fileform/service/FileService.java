package com.example.fileform.service;

import com.example.fileform.domain.File;
import com.example.fileform.dto.FileDto;
import com.example.fileform.repository.FileRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Transactional
    public Long saveFile(FileDto fileDto) {
        return fileRepository.save(fileDto.toEntity()).getId();
    }

    public FileDto getFile(Long fileId) {
        File file = fileRepository.findById(fileId).get();

        FileDto fileDto = FileDto.builder()
                .id(file.getId())
                .originalFileName(file.getOriginalFileName())
                .fileName(file.getFileName())
                .filePath(file.getFilePath())
                .build();
        return fileDto;
    }
}
