package com.example.fileform.dto;

import com.example.fileform.domain.File;
import lombok.*;

@Getter @Setter
@ToString @NoArgsConstructor
public class FileDto {

    private Long id;
    private String originalFileName;
    private String fileName;
    private String filePath;

    public File toEntity() {
        File build = File.builder()
                .id(id)
                .originalFileName(originalFileName)
                .fileName(fileName)
                .filePath(filePath)
                .build();
        return build;
    }

    @Builder
    public FileDto(Long id, String originalFileName, String fileName, String filePath) {
        this.id = id;
        this.originalFileName = originalFileName;
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
