package com.priya.Drive_BE.repo;

import com.priya.Drive_BE.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity,Long> {

}
