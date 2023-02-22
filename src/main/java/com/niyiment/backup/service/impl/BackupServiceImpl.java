package com.niyiment.backup.service.impl;


import com.niyiment.backup.domain.dto.ScheduleRequest;
import com.niyiment.backup.service.BackupService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import com.niyiment.backup.config.ApplicationProperties;
import com.niyiment.backup.config.YAMLConfig;
import com.niyiment.backup.resource.JobSchedulerResource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class BackupServiceImpl implements BackupService {
    private final JobSchedulerResource jobSchedulerResource;
    private final YAMLConfig yamlConfig;
    private static final String BASE_DIR = "/backup/";
    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
    private final JdbcTemplate jdbcTemplate;
    private final ApplicationProperties applicationProperties;
    private static  final ApplicationProperties appProperties = new ApplicationProperties();
    private static final String UPLOADED_BACKUP_NAME = "uploaded_backup_";
    private static final String BACKUP_NAME = "backup_";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private static final String IPADDRESS = "ipAddress";
    private static final String DATABASE = "database";
    private static final String PORT = "port";
    private static final String SQL_EXTENSION = ".sql";
    private String pgFileDir;
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
    SimpleDateFormat alternateDf = new SimpleDateFormat("yyyyMMdd");

    public void upload(InputStream inputStream) throws IOException {
        String directory = applicationProperties.getTempDir() + BASE_DIR;
        new File(directory).mkdir();
        Date date1 = new Date();
        String fileName = UPLOADED_BACKUP_NAME + df.format(date1) + SQL_EXTENSION;
        try (OutputStream outputStream = Files.newOutputStream(Paths.get(directory + fileName))){
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException exception) {
            log.info(exception.getMessage());
        }
    }

    public ByteArrayOutputStream downloadBackup() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String directory = applicationProperties.getTempDir() + BASE_DIR;
        Optional<String> file = listFilesUsingDirectoryStream(directory).stream()
                .filter(f -> f.endsWith(SQL_EXTENSION))
                .sorted((b1, b2) -> {
                    Date date1 = new Date();
                    Date date2 = new Date();
                    String backupName = "";
                    if (b1.contains(UPLOADED_BACKUP_NAME)) {
                        b1 = b1.replace(UPLOADED_BACKUP_NAME, "");
                        b2 = b2.replace(UPLOADED_BACKUP_NAME, "");
                    } else {
                        b1 = b1.replace("_backup_", "");
                        b2 = b2.replace(BACKUP_NAME, "");
                    }
                    try {
                        date1 = df.parse(b1.replace(SQL_EXTENSION, ""));
                        date2 = df.parse(b2.replace(SQL_EXTENSION, ""));
                    } catch (ParseException e) {
                        try {
                            date1 = alternateDf.parse(b1.replace(SQL_EXTENSION, ""));
                            date2 = alternateDf.parse(b2.replace(SQL_EXTENSION, ""));
                        } catch (ParseException ex) {
                            log.error(ex.getMessage());
                        }
                    }

                    return date2.compareTo(date1);
                })
                .limit(1)
                .findFirst();

        if (file.isPresent()) {
            try(InputStream fileInputStream = Files.newInputStream(Paths.get(directory + file.get()))){
                IOUtils.copy(fileInputStream, baos);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baos;
    }

    public ByteArrayOutputStream downloadBackup(String databaseName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String directory = applicationProperties.getTempDir() + BASE_DIR;
        try {
            Optional<String> file = listFilesUsingDirectoryStream(directory).stream()
                    .filter(f -> f.equals(databaseName))
                    .limit(1)
                    .findFirst();
            if (file.isPresent()) {
                IOUtils.copy(new FileInputStream(directory + file.get()), baos);
            }
        } catch (NoSuchElementException exception) {
            exception.printStackTrace();
        }
        return baos;
    }

    public void backupPGSQL(boolean restore) {
        try {
            log.info("Backup started");
            String directory = applicationProperties.getTempDir() + BASE_DIR;
            new File(directory).mkdir();
            pgFile();

            Map<String, String> mapper = getDatabaseProperties();
            ProcessBuilder pb;
            StringBuilder date = new StringBuilder();
            date.append(df.format(new Date()));
            File file = new File(directory);

            if (file.exists()) {
                log.info("Creating dump");
                String exec = "pg_dump";
                if (SystemUtils.IS_OS_WINDOWS) {
                    exec = pgFileDir + File.separator + exec + ".exe"; //TEMP_DIR
                }
                String buffer = directory + BACKUP_NAME + date.toString() + SQL_EXTENSION;
                if (restore) {
                    buffer = "restore.sql";
                }
                pb = new ProcessBuilder(exec, "-f", buffer,
                        "-F", "c", "-Z", "9", "-b", "-v", "-c", "-C", "-p", mapper.get(PORT), "-h",
                        mapper.get(IPADDRESS), "-U", mapper.get(USERNAME), mapper.get(DATABASE));

                pb.environment().put("PGPASSWORD", mapper.get(PASSWORD));
                pb.redirectErrorStream(true);

                try {
                    Process p = pb.start();
                    InputStream is = p.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String line; int counter = 0;
                    while ((line = br.readLine()) != null) {
                        counter++;
                    }
                    p.waitFor();
                    p.destroy();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("error {} ", e.getMessage());
                }

                log.info("status: {} ", "Backup completed");
            }
        } catch (Exception x) {
            x.printStackTrace();
            log.error("catch error: {}", x.getMessage());
        }
    }

    public void restorePGSQL(String fileName) {
        backupPGSQL(true);
        try {
            log.info("restore status: {} ", "Restore started");
            try {
                jdbcTemplate.execute("drop schema if exists public cascade; create schema public;");
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            String directory = applicationProperties.getTempDir() + BASE_DIR;
            pgFile();

            Map<String, String> mapper = getDatabaseProperties();
            ProcessBuilder pb;
            String exec = "pg_restore";
            Optional<String> file = listFilesUsingDirectoryStream(directory).stream()
                    .filter(f -> f.equals(fileName))
                    .limit(1)
                    .findFirst();

            if (file.isPresent()) {
                log.info("restore file present");
                try {
                    if (SystemUtils.IS_OS_WINDOWS) {
                        exec = pgFileDir + File.separator + exec + ".exe";
                    }

                    pb = new ProcessBuilder(exec,
                            "-F", "c", "-c", "-C", "-v", "-p", mapper.get(PORT), "-h", mapper.get(IPADDRESS),
                            "-U", mapper.get(USERNAME), "-d", mapper.get(DATABASE), directory + fileName.trim()
                    );
                    pb.environment().put("PGPASSWORD", mapper.get(PASSWORD));
                    pb.redirectErrorStream(true);
                    Process p = pb.start();
                    InputStream inputStream = p.getInputStream();
                    InputStreamReader isr = new InputStreamReader(inputStream);
                    BufferedReader br = new BufferedReader(isr);
                    String line; int counter = 0;
                    while ((line = br.readLine()) != null) {
                        counter++;
                    }
                    p.waitFor();
                    p.destroy();
                    log.info("status: {} ", "Restore completed");
                } catch (IOException | InterruptedException e) {
                    log.error("error: {}", e.getMessage());
                }
            }
        } catch (IOException x) {
            log.error("catch error: {} ", x.getMessage());
        }
    }

    @SneakyThrows
    public List<String> backupAvailable() {
        String directory = applicationProperties.getTempDir() + BASE_DIR;
        return listFilesUsingDirectoryStream(directory).stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }

    private Set<String> listFilesUsingDirectoryStream(String dir) throws IOException {
        Set<String> fileList = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    fileList.add(path.getFileName().toString());
                }
            }
        }

        return fileList;
    }

    @SneakyThrows
    public void cleanupBackup() {
        String directory = applicationProperties.getTempDir() + BASE_DIR;
        listFilesUsingDirectoryStream(directory).stream()
                .filter(f -> f.endsWith(".sql"))
                .forEach(f -> {
                    try {
                        Date date;
                        String backupName = "";
                        if (f.contains(UPLOADED_BACKUP_NAME)) {
                            backupName = f.replace(UPLOADED_BACKUP_NAME, "");
                        } else {
                            backupName = f.replace(BACKUP_NAME, "");
                        }
                        try {
                            date = df.parse(backupName.replace(SQL_EXTENSION, ""));
                        } catch (ParseException e) {
                            date = alternateDf.parse(backupName.replace(SQL_EXTENSION, ""));
                        }
                        if (LocalDateTime.now().minusDays(5).isAfter(convertToLocalDateTimeViaSqlTimestamp(date))) {
                            FileUtils.deleteQuietly(new File(directory + f));
                        }

                        if (LocalDateTime.now().minusHours(2).isAfter(convertToLocalDateTimeViaSqlTimestamp(date)) &&
                                LocalDateTime.now().toLocalDate().equals(convertToLocalDateTimeViaSqlTimestamp(date).toLocalDate())) {
                            FileUtils.deleteQuietly(new File(directory + f));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                });
    }

    private Map<String, String> getDatabaseProperties() {

        int index = yamlConfig.getUrl().indexOf("//") + 2;
        int index2 = yamlConfig.getUrl().lastIndexOf(":");
        int lastIndex = yamlConfig.getUrl().lastIndexOf("/") + 1;
        String dbase = yamlConfig.getUrl().substring(lastIndex);
        if (yamlConfig.getUrl().contains("?")) {
            int index3 = yamlConfig.getUrl().indexOf("?");
            dbase = yamlConfig.getUrl().substring(lastIndex, index3);
        }

        String ip = yamlConfig.getUrl().substring(index, index2);
        String user = yamlConfig.getUsername();
        String password = yamlConfig.getPassword();
        String port = yamlConfig.getUrl().substring(index2 + 1, lastIndex - 1);
        Map<String, String> mapper = new HashMap<>();
        mapper.put(IPADDRESS, ip);
        mapper.put(USERNAME, user);
        mapper.put(DATABASE, dbase);
        mapper.put(PASSWORD, password);
        mapper.put(PORT, port);

        return mapper;
    }

    @Scheduled(cron = "0 */2 * * * ?")
    public void backupCleanup() {
        try {
            Thread.sleep(1000);
            backupPGSQL(false);
            cleanupBackup();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @SneakyThrows
    @PostConstruct
    public void init() {
        String directory = applicationProperties.getTempDir() + BASE_DIR;
        new File(directory).mkdir();

        String jobClass = "com.niyiment.backup.service.impl.BackupJob";
        boolean scheduled = jobSchedulerResource.listJobClasses()
                .stream()
                .anyMatch(c -> c.equals(jobClass));
        if (!scheduled) {
            ScheduleRequest request = new ScheduleRequest();
            request.setJobClass(jobClass);
            request.setCronExpression("0 */2 * * * ?");
            jobSchedulerResource.scheduleJob(request);
        }

    }

    public void backupAndRestoreDatabase(String operationType) {
        Map<String, String> mapper = getDatabaseProperties();

        String urlpsql = "jdbc:postgresql://" +  mapper.get(IPADDRESS) +  ":" + mapper.get(PORT) + "/" + mapper.get(DATABASE);

        try {
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException e) {
            log.info("Class not found: "+ e.getMessage());
            return;
        }
        try (Connection connection = DriverManager.getConnection(urlpsql, mapper.get(USERNAME), mapper.get(PASSWORD));) {
            if (mapper.get(DATABASE) != null) {
                if (operationType != null && operationType.equalsIgnoreCase("Backup")) {
                    String[] executeCmd =  new String[]{"pg_dump -U " + mapper.get(USERNAME) + " -w -c -f " + mapper.get(DATABASE) + ".sql " + mapper.get(DATABASE)};
                    executeBackupCommand(executeCmd, "Backup created successfully");

                } else if (operationType == null || operationType.equalsIgnoreCase("Restore")) {
                    String[] executeCmd = new String[]{"psql", "--username=" + mapper.get(USERNAME), "--file=" + mapper.get(DATABASE) + SQL_EXTENSION, mapper.get(DATABASE)};
                    executeBackupCommand(executeCmd, "Backup restored successfully");
                }
            }

        } catch (SQLException e) {
            log.info(e.getMessage());
        }
    }

    public void executeBackupCommand(String[] executeCmd, String message) {
        Process runtimeProcess;
        try {
            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();
            if (processComplete == 0) {
                log.info(message);
            } else {
                log.info("Could not create the backup: " + processComplete);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }
    private LocalDateTime convertToLocalDateTimeViaSqlTimestamp(Date dateToConvert) {
        return new Timestamp(
                dateToConvert.getTime()).toLocalDateTime();
    }

    @PostConstruct
    private void pgFile() throws IOException {
        pgFileDir = Files.createTempDirectory("pg").toFile().getAbsolutePath();
        new File(pgFileDir).mkdir();
        String[] files = {"pg_dump.exe", "pg_restore.exe", "libcrypto-1_1-x64.dll",
                "libpq.dll", "libssl-1_1-x64.dll", "libwinpthread-1.dll", "zlib1.dll", "libiconv-2.dll", "libintl-9.dll"};

        for (String file : files) {
            String filePath = "pg" + File.separator + file;
            try (InputStream is = BackupServiceImpl.class.getClassLoader().getResourceAsStream(filePath);
                 FileOutputStream fos = new FileOutputStream(new File(pgFileDir + File.separator + file)); ) {

                assert is != null;
                IOUtils.copyLarge(is, fos);
            } catch (IOException e) {
                log.info("Error message: {}", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public String getFileExtension(String fullName) {
        if (fullName != null) {
            String fileName = new File(fullName).getName();
            int dotIndex = fileName.lastIndexOf('.');
            
            return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
        }
        
        return "";
    }

}

