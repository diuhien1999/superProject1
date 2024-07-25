package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.FileModel;
import com.udacity.jwdnd.course1.cloudstorage.model.UserModel;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;
    private final UserMapper userMapper;

    public FileController(FileService fileService, UserService userService, UserMapper userMapper) {
        this.fileService = fileService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication authentication, RedirectAttributes redirectAttributes) throws IOException {
        String uploadError = null;

        String loggedInUserName = (String) authentication.getPrincipal();
        UserModel user = userMapper.getUser(loggedInUserName);

        if (fileUpload.isEmpty()) {
            uploadError = "Please select a non-empty file.";
        }

        List<FileModel> listFile =  fileService.getFileByFileName(fileUpload.getOriginalFilename(), user.getUserId());
        if (!listFile.isEmpty()) {
            uploadError = "This file has exist. Please select another file.";
        }

        if(uploadError!=null) {
            redirectAttributes.addFlashAttribute("error", uploadError);
            return "redirect:/result?error";
        }

        try {
            fileService.addFile(fileUpload, user.getUserId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/home";
    }

    @GetMapping("/view/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer fileId){
        FileModel file = fileService.getFileById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ file.getFileName()+"\"")
                .body(new ByteArrayResource(file.getFileData()));
    }

    @GetMapping("/delete")
    public String deleteFile(@RequestParam("id") int fileid, Authentication authentication, RedirectAttributes redirectAttributes){
        String loggedInUserName = (String) authentication.getPrincipal();
        UserModel user = userMapper.getUser(loggedInUserName);
        String deleteError = null;

        if(fileid > 0){
            fileService.deleteFile(fileid);
            return "redirect:/home";
        }

        redirectAttributes.addAttribute("error", "Unable to delete the file.");
        return "redirect:/result?error";
    }
}
