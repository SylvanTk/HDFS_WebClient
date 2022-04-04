package com.company.Response.ListDirectoryResponse;


import com.company.Enums.ObjectType;

public class FileStatus {
    public long accessTime;
    public int blockSize;
    public int childrenNum;
    public int fileId;
    public String group;
    public int length;
    public long modificationTime;
    public String owner;
    public String pathSuffix;
    public String permission;
    public int replication;
    public int storagePolicy;
    public ObjectType type;


    public int getChildrenNum() {
        return childrenNum;
    }

    public void setChildrenNum(int childrenNum) {
        this.childrenNum = childrenNum;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getStoragePolicy() {
        return storagePolicy;
    }

    public void setStoragePolicy(int storagePolicy) {
        this.storagePolicy = storagePolicy;
    }

    public long getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(long accessTime) {
        this.accessTime = accessTime;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(long modificationTime) {
        this.modificationTime = modificationTime;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPathSuffix() {
        return pathSuffix;
    }

    public void setPathSuffix(String pathSuffix) {
        this.pathSuffix = pathSuffix;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public int getReplication() {
        return replication;
    }

    public void setReplication(int replication) {
        this.replication = replication;
    }

    public ObjectType getType() {
        return type;
    }

    public void setType(ObjectType type) {
        this.type = type;
    }
}
