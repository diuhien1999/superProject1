package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialModel;
import com.udacity.jwdnd.course1.cloudstorage.model.UserModel;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller

public class HomeController {

    @Autowired
    private AuthenticationService authenticationService;
    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    public final EncryptionService encryptionService;

    public HomeController(FileService fileService, UserMapper userMapper, CredentialService credentialsService, NoteService noteService, CredentialService credentialService, EncryptionService encryptionService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        UserModel user = authenticationService.user;

        List<CredentialModel> credentials = credentialService.getCredentials(user.getUserId());

        for (CredentialModel credential : credentials) {
            String decryptedPassword = encryptionService.decryptValue(credential.getPassword(), credential.getKey());
            credential.setDeccryptedPassword(decryptedPassword);
        }

        model.addAttribute("files", fileService.getUploadedFiles(user.getUserId()));
        model.addAttribute("notes", noteService.getNotes(user.getUserId()));
        model.addAttribute("credentials", credentials);
        model.addAttribute("encryptionService", encryptionService);

        return "home";
    }
}
