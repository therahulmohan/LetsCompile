package com.lets.compile.letscompileback.services;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class CompilerServiceForPython {
    public String compileCode(String code, String input) throws IOException, InterruptedException {
        String univString = UUID.randomUUID().toString();
        String home = System.getenv("PY_HOME");
        if (home.endsWith("\\") || home.endsWith("/")) {
            home = home + "bin" + File.separator + "python.exe";
        } else {
            home = home + File.separator + "bin" + File.separator + "python.exe";
        }
        String src = univString.concat(".py");
        File tmpFolder = new File("tmpFolder");
        if (tmpFolder.exists() == false)
            tmpFolder.mkdir();
        File srcFile = new File(tmpFolder.getAbsolutePath() + File.separator + src);
        RandomAccessFile randomAccessFile = new RandomAccessFile(srcFile,
                "rw");
        randomAccessFile.writeBytes(code);
        randomAccessFile.close();
        java.util.List<String> commands = new ArrayList<>();
        commands.add("py");
        commands.add(tmpFolder.getAbsolutePath() + File.separator + src);
        File inputFile = new File(tmpFolder.getAbsolutePath() + File.separator + univString
                + "input");
        randomAccessFile = new RandomAccessFile(inputFile, "rw");
        randomAccessFile.writeBytes(input);
        randomAccessFile.close();
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(commands);
        String output = univString.concat(".output");
        File outputFile = new File(tmpFolder.getAbsolutePath() + File.separator
                + output);
        processBuilder.redirectOutput(outputFile);
        processBuilder.redirectInput(inputFile);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        process.waitFor();
        StringBuilder result = new StringBuilder(
                "");
        randomAccessFile = new RandomAccessFile(outputFile, "r");
        while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
            result.append(randomAccessFile.readLine() + "\r\n");
        }
        randomAccessFile.close();
        return result.toString();
    }
}
