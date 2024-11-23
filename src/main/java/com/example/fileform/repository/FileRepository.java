package com.example.fileform.repository;

import com.example.fileform.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.nio.ByteBuffer;

public interface FileRepository extends JpaRepository<File,Long> {
}
