package com.lets.compile.letscompileback.services;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class CompilerServiceForCPP {
    public String compileCode(String code, String input) throws IOException, InterruptedException {
        String univString = UUID.randomUUID().toString();
        String home = System.getenv("G++_HOME");
        if (home.endsWith("\\") || home.endsWith("/")) {
            home = home + "bin" + File.separator + "g++.exe";
        } else {
            home = home + File.separator + "bin" + File.separator + "g++.exe";
        }
        String src = univString.concat(".cpp");
        File tmpFolder = new File("tmpFolder");
        if (tmpFolder.exists() == false)
            tmpFolder.mkdir();
        File srcFile = new File(tmpFolder.getAbsolutePath() + File.separator + src);
        RandomAccessFile randomAccessFile = new RandomAccessFile(srcFile, "rw");
        randomAccessFile.writeBytes(code);
        randomAccessFile.close();
        String executable = univString.concat(".exe");
        String stderr = univString.concat(".err");
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(tmpFolder);
        java.util.List<String> commands = new ArrayList<>();
        commands.add(home);
        commands.add(src);
        commands.add("-o");
        commands.add(executable);
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
        commands.add(tmpFolder.getAbsolutePath()+File.separator+executable);
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
