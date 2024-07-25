package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialModel;
import com.udacity.jwdnd.course1.cloudstorage.model.UserModel;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/credential")

public class CredentialController {
    private final UserMapper userMapper;
    private final CredentialService credentialService;

    public CredentialController(UserMapper userMapper, CredentialService credentialService) {
        this.userMapper = userMapper;
        this.credentialService = credentialService;
    }

    @PostMapping
    public String handleAddUpdateCredentials(Authentication authentication, CredentialModel credential){
        String loggedInUserName = (String) authentication.getPrincipal();
        UserModel user = userMapper.getUser(loggedInUserName);
        Integer userId = user.getUserId();

        if (credential.getCredentialId()  != 0) {
            credentialService.editCredential(credential);
        } else {
            credentialService.addCredential(credential, userId);
        }

        return "redirect:/home";
    }

    @GetMapping("/delete")
    public String deleteCredentials(@RequestParam("id") int credentialid, Authentication authentication, RedirectAttributes redirectAttributes){
        String loggedInUserName = (String) authentication.getPrincipal();
        UserModel user = userMapper.getUser(loggedInUserName);

        if(credentialid > 0){
            credentialService.deleteCredentials(credentialid);
            return "redirect:/home";
        }

        redirectAttributes.addAttribute("error", "Unable to delete the credentials.");
        return "redirect:/result?error";
    }
}
