package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.FileModel;
import com.udacity.jwdnd.course1.cloudstorage.model.UserModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper, UserMapper userMapper) {
        this.fileMapper = fileMapper;
    }

    public List<FileModel> getUploadedFiles(Integer userId){
        return fileMapper.getFileByUser(userId);
    }

    public List<FileModel> getFileByFileName(String fileName, Integer userId){
        return fileMapper.getFileByFileName(fileName, userId);
    }

    public void addFile(MultipartFile fileUpload, int userid) throws IOException {
        FileModel file = new FileModel();

        try {
            file.setContentType(fileUpload.getContentType());
            file.setFileData(fileUpload.getBytes());
            file.setFileName(fileUpload.getOriginalFilename());
            file.setFileSize(Long.toString(fileUpload.getSize()));
            file.setUserId(userid);
        } catch (IOException e) {
            throw e;
        }
        int a = fileMapper.storeFile(file);
    }

    public FileModel getFileById(Integer fileId){
        return fileMapper.getFileById(fileId);
    }

    public int deleteFile(int fileId) {
        return fileMapper.deleteFile(fileId);
    }
}
