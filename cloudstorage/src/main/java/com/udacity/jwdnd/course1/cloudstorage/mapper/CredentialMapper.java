package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE userId= #{userId}")
    List<CredentialModel> getCredentials(Integer userId);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    CredentialModel getCredentialById(int credentialId);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userId) VALUES (#{url}, #{username}, #{key}, #{password},#{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insertCredentilas(CredentialModel credentials);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, key = #{key}, password= #{password} WHERE credentialId = #{credentialId}")
    void updateCredentilas(CredentialModel credentials);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    int deleteCredentilas(int credentialId);
}
