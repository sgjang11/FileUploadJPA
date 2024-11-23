package com.example.fileform.controller;

import com.example.fileform.dto.BoardDto;
import com.example.fileform.dto.FileDto;
import com.example.fileform.service.BoardService;
import com.example.fileform.service.FileService;
import com.example.fileform.util.MD5Generator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Controller
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;

    public BoardController(BoardService boardService, FileService fileService) {
        this.boardService = boardService;
        this.fileService = fileService;
    }

    @GetMapping("/")
    public String list(Model model) {
        List<BoardDto> boardDtoList = boardService.getBoardList();
        model.addAttribute("postList", boardDtoList);
        return "board/list";
    }

    @GetMapping("/post")
    public String post() {
        return "board/post";
    }

    @PostMapping("/post")
    public String write(@RequestParam("file") MultipartFile files, BoardDto boardDto) {
        String originalFilename = files.getOriginalFilename();
        System.out.println(originalFilename);
        try {
            String filename = new MD5Generator(originalFilename).toString();
            String savePath = System.getProperty("user.dir") + "\\files";

            if(!new File(savePath).exists()){
                try {
                    new File(savePath).mkdir();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            String filePath = savePath + "\\" + filename;
            files.transferTo(new File(filePath));

            FileDto fileDto = new FileDto();
            fileDto.setOriginalFileName(originalFilename);
            fileDto.setFileName(filename);
            fileDto.setFilePath(filePath);

            Long fileId = fileService.saveFile(fileDto);
            boardDto.setFileId(fileId);
            boardService.savePost(boardDto);

        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);
        FileDto fileDto = fileService.getFile(boardDto.getFileId());
        String filename = fileDto.getOriginalFileName();
        model.addAttribute("post", boardDto);
        model.addAttribute("filename", filename);
        return "board/detail";
    }

    @GetMapping("/post/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);
        model.addAttribute("post", boardDto);
        return "board/edit";
    }

    @PutMapping("/post/edit/{id}")
    public String update(BoardDto boardDto) {
        boardService.savePost(boardDto);
        return "redirect:/";
    }

    @DeleteMapping("/post/{id}")
    public String delete(@PathVariable("id") Long id) {
        boardService.deletePost(id);
        return "redirect:/";
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> fileDownload(@PathVariable("fileId") Long fileId) throws IOException {
        FileDto fileDto = fileService.getFile(fileId);
        Path path = Paths.get(fileDto.getFilePath());
        Resource resource = new InputStreamResource(Files.newInputStream(path));
        String encodedFileName = UriUtils.encode(fileDto.getOriginalFileName(), StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                .body(resource);
    }

}
