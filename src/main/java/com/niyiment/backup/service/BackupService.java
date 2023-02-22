package com.niyiment.backup.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public interface BackupService {
    void upload(InputStream inputStream) throws IOException;
    ByteArrayOutputStream downloadBackup() throws IOException;
    ByteArrayOutputStream downloadBackup(String databaseName) throws IOException;
    void backupPGSQL(boolean restore);
    void restorePGSQL(String fileName);
    List<String> backupAvailable();
    void cleanupBackup();
    void backupCleanup();
    void init();
    void backupAndRestoreDatabase(String operationType);
    String getFileExtension(String fullName);

}
