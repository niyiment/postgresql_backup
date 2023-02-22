package com.niyiment.backup.resource;

import com.niyiment.backup.service.BackupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BackupResource {
    private final BackupService backupService;

    @PostMapping("/backup/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            backupService.upload(file.getInputStream());

            return new ResponseEntity<>("Uploaded the file successfully: " + file.getOriginalFilename(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Could not upload the file: " + file.getOriginalFilename() + "!", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/backup/restore/{fileName}")
    public ResponseEntity<String> restore(@PathVariable @Valid String fileName) {
        try {
            if (!backupService.getFileExtension(fileName).equalsIgnoreCase("sql")
                    || fileName == null || !fileName.contains(".sql")) {
                return new ResponseEntity<>("Oops! Invalid filename.", HttpStatus.EXPECTATION_FAILED);
            }
            backupService.restorePGSQL(fileName);
            return new ResponseEntity<>("Database restored successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }

    }

    @GetMapping("/backup/backup")
    public ResponseEntity<String> backup() {
        try {
            backupService.backupPGSQL(false);
            return new ResponseEntity<>("Database backup completed successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/backup/backup-and-restore/{type}")
    public ResponseEntity<String> backupAndRestore(@PathVariable @Valid String type) {
        try {
            backupService.backupAndRestoreDatabase(type);
            return new ResponseEntity<>("Database backup completed successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/backup/download")
    public void downloadBackup(HttpServletResponse response) throws IOException {
        ByteArrayOutputStream baos = backupService.downloadBackup();
        response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("Content-Length", Integer.valueOf(baos.size()).toString());
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(baos.toByteArray());
        outputStream.close();
        response.flushBuffer();
    }

    @GetMapping("/backup/download/{databaseName}")
    public void downloadBackup(HttpServletResponse response, @PathVariable String databaseName) throws IOException {
        try {
            if (!backupService.getFileExtension(databaseName).equalsIgnoreCase("sql") || databaseName == null) {
                throw new IOException("Invalid request. Try again");
            }

            ByteArrayOutputStream baos = backupService.downloadBackup(databaseName);
            if (baos.size() > 10000) {
                response.setHeader("Content-Type", "application/octet-stream");
                response.setHeader("Content-Length", Integer.valueOf(baos.size()).toString());
                OutputStream outputStream = response.getOutputStream();
                outputStream.write(baos.toByteArray());
                outputStream.close();
                response.flushBuffer();
                log.info("Database download completed.");
            } else {
                response.setHeader("Content-Type", "application/octet-stream");
                response.sendError(500, "Database download failed. Invalid database backup");
                log.info("Database download failed.");
            }
        } catch (Exception e) {
            response.setHeader("Content-Type", "application/octet-stream");
            response.sendError(500, e.getMessage());
        }

    }

    @GetMapping("/backup/backup-available")
    public ResponseEntity<List<String>> backupAvailable() {
        return new ResponseEntity<>(backupService.backupAvailable(), HttpStatus.OK);
    }

}