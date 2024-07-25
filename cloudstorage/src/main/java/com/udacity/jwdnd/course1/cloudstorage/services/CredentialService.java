package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialModel;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private final EncryptionService encryptionService;
    private final CredentialMapper credentialsMapper;

    public CredentialService(EncryptionService encryptionService, CredentialMapper credentialsMapper) {
        this.encryptionService = encryptionService;
        this.credentialsMapper = credentialsMapper;
    }

    public void addCredential(CredentialModel credential, int userId){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);

        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        CredentialModel newCredentials = new CredentialModel();
        newCredentials.setUrl(credential.getUrl());
        newCredentials.setUsername(credential.getUsername());
        newCredentials.setKey(encodedKey);
        newCredentials.setPassword(encryptedPassword);
        newCredentials.setUserId(userId);

        credentialsMapper.insertCredentilas(newCredentials);
    }

    public void editCredential(CredentialModel credential){
        CredentialModel storedCredential = credentialsMapper.getCredentialById(credential.getCredentialId());
        credential.setKey(storedCredential.getKey());

        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), credential.getKey());
        credential.setPassword(encryptedPassword);
        credentialsMapper.updateCredentilas(credential);
    }

    public int deleteCredentials(int credentialid){
        return credentialsMapper.deleteCredentilas(credentialid);
    }

    public List<CredentialModel> getCredentials(int userid){
        return credentialsMapper.getCredentials(userid);
    }
}
