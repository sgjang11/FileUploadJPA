package com.example.fileform.dto;

import com.example.fileform.domain.Board;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@ToString @NoArgsConstructor
public class BoardDto {
    private Long id;
    private String author;
    private String title;
    private String content;
    private Long fileId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public Board toEntity() {
        Board build = Board.builder()
                .id(id)
                .author(author)
                .title(title)
                .content(content)
                .fileId(fileId)
                .build();
        return build;
    }

    @Builder
    public BoardDto(Long id, String author, String title, String content, long fileId, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.fileId = fileId;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
