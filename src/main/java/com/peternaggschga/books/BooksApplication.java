package com.peternaggschga.books;

import lombok.NonNull;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Main application, configuring and starting {@link SpringApplication Spring}.
 */
@SpringBootApplication
public class BooksApplication {
    private static final Logger LOG = LoggerFactory.getLogger(BooksApplication.class);

    public static void main(String[] args) {
        try {
            saveDatabaseBackup(new File("db/books.mv.db"), new File("db/backup/"));
        } catch (IOException e) {
            LOG.error("Fehler beim Datenbankbackup: " + e);
            e.printStackTrace();
        }
        SpringApplication.run(BooksApplication.class, args);
    }

    private static void saveDatabaseBackup(@NonNull File currentDatabase, @NonNull File backupDirectory)
            throws IOException {
        if (!(backupDirectory.mkdirs() || backupDirectory.exists())) {
            return;
        }
        File backupFile = new File(backupDirectory.getPath() + '/'
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")) + ".mv.db");
        File[] backupFiles = backupDirectory.listFiles();
        if (backupFiles != null && backupFiles.length >= 20) {
            File oldestBackup = backupFiles[0];
            for (File file : backupFiles) {
                if (file.lastModified() < oldestBackup.lastModified()) {
                    oldestBackup = file;
                }
            }
            FileSystemUtils.deleteRecursively(oldestBackup);
        }
        if (currentDatabase.exists() && !backupFile.exists()) {
            FileSystemUtils.copyRecursively(currentDatabase, backupFile);
        }
    }

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

    @Configuration
    static class ApplicationWebConfig implements WebMvcConfigurer {

        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            registry.addViewController("/").setViewName("reading/readings");
        }
    }

    @Configuration
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public static class SecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity security) throws Exception {
            security.authorizeRequests().antMatchers("/**").permitAll();
        }
    }
}
