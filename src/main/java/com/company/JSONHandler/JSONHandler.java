package com.company.JSONHandler;

import com.company.Response.BooleanResponse.BooleanResponse;
import com.company.Extensions.ConsoleColors;
import com.company.Response.ListDirectoryResponse.FileStatus;
import com.company.Response.ListDirectoryResponse.ListDirectoryResponse;
import com.company.Enums.ObjectType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class JSONHandler {
    private String JSON;
    private ObjectMapper mapper;

    private static ListDirectoryResponse listDirectoryResponse;
    private BooleanResponse booleanResponse;


    public JSONHandler(String JSON){
        this.JSON = JSON;
        this.mapper = new ObjectMapper();
    }

    public JSONHandler(){
        this.mapper = new ObjectMapper();
    }

    public void InputJSON(String json){
        this.JSON = json;
    }

    public JSONHandler DeserializeBoolean(){
        booleanResponse = new BooleanResponse();
        try {
            booleanResponse = mapper.readValue(JSON, BooleanResponse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return this;
    }

    public JSONHandler DeserializeFileStatuses(){
        listDirectoryResponse = new ListDirectoryResponse();
        try {
            listDirectoryResponse = mapper.readValue(JSON, ListDirectoryResponse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return this;
    }

    public void PrintDirs(){
        List<FileStatus> fileStatuses = listDirectoryResponse.getFileStatuses().getFileStatus();

        if(fileStatuses.isEmpty())
            System.out.println(ConsoleColors.MakePurple("Пусто!"));
        else
            for (FileStatus fileStatus : fileStatuses)
                System.out.println((fileStatus.type == ObjectType.DIRECTORY ? ConsoleColors.MakeCyan(fileStatus.getPathSuffix()) : ConsoleColors.MakeGreen(fileStatus.getPathSuffix())));
    }

    public void PrintBoolean(){
        System.out.println(booleanResponse.getBoolean().equals("true") ? ConsoleColors.MakeBlue("OK") : ConsoleColors.MakeRed("BAD"));
    }

    public String[] GetDirs(){
        List<FileStatus> fileStatuses = listDirectoryResponse.getFileStatuses().getFileStatus();

        ArrayList<String> dirs = new ArrayList<>();

        for (FileStatus fileStatus : fileStatuses)
            if(fileStatus.type == ObjectType.DIRECTORY)
                dirs.add(fileStatus.getPathSuffix());

        return dirs.toArray(new String[0]);
    }

    public String[] GetFiles(){
        List<FileStatus> fileStatuses = listDirectoryResponse.getFileStatuses().getFileStatus();

        ArrayList<String> files = new ArrayList<>();

        for (FileStatus fileStatus : fileStatuses)
            if(fileStatus.type == ObjectType.FILE)
                files.add(fileStatus.getPathSuffix());

        return files.toArray(new String[0]);
    }

    public String[] GetAllObjects(){
        List<FileStatus> fileStatuses = listDirectoryResponse.getFileStatuses().getFileStatus();
        ArrayList<String> objects = new ArrayList<>();

        for (FileStatus fileStatus : fileStatuses)
                objects.add(fileStatus.getPathSuffix());

        return objects.toArray(new String[0]);
    }

    public boolean fileExists(String fileName){
        String[] files = this.GetFiles();
        boolean fileExists = false;

        for(String file : files)
            if(fileExists)
                break;
            else
            if(file.equals(fileName))
                fileExists = true;


        return fileExists;
    }

    public boolean directoryExists(String directoryName){
        String[] dirs = this.GetDirs();
        boolean directoryExists = false;

        for(String dir : dirs)
            if(directoryExists)
                break;
            else
                if(dir.equals(directoryName))
                    directoryExists = true;

        return directoryExists;
    }

    public boolean objectExists(String objectName){
        String[] objects = this.GetAllObjects();
        boolean objectExists = false;

        for(String dir : objects)
            if(objectExists)
                break;
            else
            if(dir.equals(objectName))
                objectExists = true;

        return objectExists;
    }

}
