package com.company.Response.ListDirectoryResponse;

import java.util.List;

public class FileStatuses {
    public List<FileStatus> FileStatus;

    public List<com.company.Response.ListDirectoryResponse.FileStatus> getFileStatus() {
        return FileStatus;
    }

    public void setFileStatus(List<com.company.Response.ListDirectoryResponse.FileStatus> fileStatus) {
        FileStatus = fileStatus;
    }
}
