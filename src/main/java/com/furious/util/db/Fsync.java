package com.furious.util.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.furious.util.Throwables;

/**
 * 同步内存中数据到磁盘
 */
class Fsync {

    private final String path;
    private final String fileName;

    public Fsync(String path) {
        this.path = path;
        this.fileName = "backup_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss")) + ".sql";
    }

    @SuppressWarnings("unused")
    public void sync(String s) {
        File dir = new File(path);
        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
        }
        File file = new File(dir, fileName);
        try {
            boolean newFile = file.createNewFile();
            try (OutputStream outputStream = new FileOutputStream(file, true)) {
                outputStream.write(s.getBytes());
                outputStream.flush();
            }
        } catch (IOException e) {
            Throwables.raise(e);
        }
    }
}
