package com.niyiment.backup.service.impl;

import com.niyiment.backup.service.BackupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.niyiment.backup.config.ContextProvider;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
@RequiredArgsConstructor
public class BackupJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.debug("Running backup job...");
        BackupService backupService = ContextProvider.getBean(BackupService.class);
        backupService.backupPGSQL(false);
        backupService.cleanupBackup();
    }
}
