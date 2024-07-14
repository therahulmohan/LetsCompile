package com.lets.compile.letscompileback.services;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class CompilerServiceForJava {
    public String compileCode(String code, String input) throws IOException, InterruptedException {
        String univString = UUID.randomUUID().toString();
        String home = System.getenv("JAVAC_HOME");
        if (home.endsWith("\\") || home.endsWith("/")) {
            home = home + "bin" + File.separator + "javac.exe";
        } else {
            home = home + File.separator + "bin" + File.separator + "javac.exe";
        }
        String src = "Main.java";
        File tmpFolder = new File("tmpFolder");
        if (tmpFolder.exists() == false)
            tmpFolder.mkdir();
        File srcFile = new File(tmpFolder.getAbsolutePath() + File.separator + src);
        RandomAccessFile randomAccessFile = new RandomAccessFile(srcFile, "rw");
        randomAccessFile.writeBytes(code);
        randomAccessFile.close();
        String stderr = univString.concat(".err");
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(tmpFolder);
        java.util.List<String> commands = new ArrayList<>();
        commands.add(home);
        commands.add(src);
        processBuilder.command(commands);
        File errorFile = new File(tmpFolder.getAbsolutePath() + File.separator + stderr);
        processBuilder.redirectOutput(errorFile);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        process.waitFor();
        randomAccessFile = new RandomAccessFile(errorFile, "r");
        StringBuilder error = new StringBuilder("");
        if (randomAccessFile.length() > 0) {
            while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
                error.append(randomAccessFile.readLine() + "\r\n");
            }
            randomAccessFile.close();
            if (srcFile.exists())
                srcFile.delete();
            if (errorFile.exists())
                errorFile.delete();
            return error.toString();
        }
        if (srcFile.exists())
            srcFile.delete();
        if (errorFile.exists())
            errorFile.delete();
        randomAccessFile.close();
        commands.clear();
        commands.add("java");
        commands.add("-classpath");
        commands.add(tmpFolder.getAbsolutePath());
        commands.add("Main");
        File inputFile=new File(tmpFolder.getAbsolutePath()+File.separator+univString+"input");
        randomAccessFile = new RandomAccessFile(inputFile, "rw");
        randomAccessFile.writeBytes(input);
        randomAccessFile.close();
        processBuilder=new ProcessBuilder();
        processBuilder.command(commands);
        String output=univString.concat(".output");
        File outputFile=new File(tmpFolder.getAbsolutePath()+File.separator+output);
        processBuilder.redirectOutput(outputFile);
        processBuilder.redirectInput(inputFile);
        processBuilder.redirectErrorStream(true);
        process=processBuilder.start();
        process.waitFor();
        StringBuilder result=new StringBuilder("");
        randomAccessFile=new RandomAccessFile(outputFile, "r");
        while (randomAccessFile.getFilePointer()<randomAccessFile.length()) {
            result.append(randomAccessFile.readLine()+"\r\n");
        }
        randomAccessFile.close();
        return result.toString();
    }

}
