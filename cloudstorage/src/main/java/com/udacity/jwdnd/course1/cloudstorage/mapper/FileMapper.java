package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.FileModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE userid= #{userid} ")
    List<FileModel> getFileByUser(Integer userid);

    @Select("SELECT * FROM FILES WHERE fileName= #{fileName} AND userid= #{userid}")
    List<FileModel> getFileByFileName(String fileName, Integer userid);

    @Select("SELECT * FROM FILES WHERE userid= #{userid} and  fileId = #{fileName}")
    FileModel getFileByNameId(String fileName, Integer userid);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES (#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int storeFile(FileModel file);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    FileModel getFileById(Integer fileId);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    int deleteFile(int fileId);
}
