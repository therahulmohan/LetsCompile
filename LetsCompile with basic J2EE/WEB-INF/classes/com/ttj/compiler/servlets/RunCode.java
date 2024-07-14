package com.ttj.compiler.servlets;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.ttj.compiler.languages.*;

public class RunCode extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            String language = request.getParameter("languages");
            String home = "";
            String univString = UUID.randomUUID().toString();
            String src = univString;
            String output = univString;
            String error = univString;
            if (language.equals("C")) {
                home = System.getenv("GCC_HOME");
                if (home.endsWith("\\") || home.endsWith("/")) {
                    home = home + "bin" + File.separator + "gcc.exe";
                } else {
                    home = home + File.separator + "bin" + File.separator + "gcc.exe";
                }
                src = src.concat(".c");
            } else if (language.equals("C++")) {
                home = System.getenv("G++_HOME");
                if (home.endsWith("\\") || home.endsWith("/")) {
                    home = home + "bin" + File.separator + "g++.exe";
                } else {
                    home = home + File.separator + "bin" + File.separator + "g++.exe";
                }
                src = src.concat(".cpp");
            } else if (language.equals("Java")) {
                home = System.getenv("JAVAC_HOME");
                if (home.endsWith("\\") || home.endsWith("/")) {
                    home = home + "bin" + File.separator + "javac.exe";
                } else {
                    home = home + File.separator + "bin" + File.separator + "javac.exe";
                }
                src = "Main.java";
            } else if (language.equals("Python")) {
                home = System.getenv("PY_HOME");
                if (home.endsWith("\\") || home.endsWith("/")) {
                    home = home + "python.exe";
                } else {
                    home = home + File.separator + "python.exe";
                }
                src = src.concat(".py");
            }
            String code = request.getParameter("codeArea");
            String inputData = request.getParameter("inputData");
            File tmpFolder = new File("C:" + File.separator + "tomcat9" + File.separator + "webapps" + File.separator + "LetsCompile" + File.separator + "tmp");
            if (tmpFolder.exists() == false) tmpFolder.mkdir();
            File srcFile = new File("C:" + File.separator + "tomcat9" + File.separator + "webapps" + File.separator + "LetsCompile" + File.separator + "tmp" + File.separator + src);
            RandomAccessFile raf = new RandomAccessFile(srcFile, "rw");
            raf.writeBytes(code);
            raf.close();
            if (language.equals("Python") == false) {
                ProcessBuilder pb = new ProcessBuilder();
                pb.directory(tmpFolder);
                java.util.List<String> list = new ArrayList<>();
                list.add(home);
                list.add(src);
                if (language.equals("C") || language.equals("C++")) {
                    output = output.concat(".exe");
                    list.add("-o");
                    list.add(output);
                }
                pb.command(list);
                error = error.concat("Err.data");
                File stderr = new File("C:" + File.separator + "tomcat9" + File.separator + "webapps" + File.separator + "LetsCompile" + File.separator + "tmp" + File.separator + error);
                pb.redirectOutput(stderr);
                pb.redirectErrorStream(true);
                Process process = pb.start();
                process.waitFor();
                raf = new RandomAccessFile(stderr, "r");
                if (raf.length() > 0) {
                    PrintWriter pw = response.getWriter();
                    response.setContentType("text/html");
                    pw.println("<!DOCTYPE>");
                    pw.println("<html lang='en'>");
                    pw.println("<head>");
                    pw.println("<meta charset='UTF-8'>");
                    pw.println("<title>Lets Compile</title>");
                    pw.println("<link rel='stylesheet' type='text/css' href='/LetsCompile/css/styles.css'>");
                    pw.println("<script src='/LetsCompile/js/LanguageChange.js'></script>");
                    pw.println("</head>");
                    pw.println("<body>");
                    pw.println("<!-- Main conatiner starts here -->");
                    pw.println("<div id='main-container'>");
                    pw.println("<!-- Header starts here -->");
                    pw.println("<div id='header'>");
                    pw.println("<img src='/LetsCompile/images/logo.png' width:'15' height:'15' id='logo'>");
                    pw.println("<div id='heading'>LetsCompile</div>");
                    pw.println("</div> <!-- Header ends here -->");
                    pw.println("<!-- content section starts here -->");
                    pw.println("<form method='post' action='/LetsCompile/runCode'>");
                    pw.println("<div id='content-section'>");
                    pw.println("&nbsp;&nbsp;&nbsp;<label for='languages'>Choose a language</label>");
                    pw.println("<select id='languages' name='languages' onchange='langChange();'>");
                    if (language.equals("C"))
                        pw.println("<option selected value='C'>C</option>");
                    else
                        pw.println("<option value='C'>C</option>");
                    if (language.equals("C++"))
                        pw.println("<option selected value='C++'>C++</option>");
                    else
                        pw.println("<option value='C++'>C++</option>");
                    if (language.equals("Java"))
                        pw.println("<option selected value='Java'>Java</option>");
                    else
                        pw.println("<option value='Java'>Java</option>");
                    if (language.equals("Python"))
                        pw.println("<option selected value='Python'>Python</option>");
                    else
                        pw.println("<option value='Python'>Python</option>");
                    pw.println("</select>");
                    pw.println("</div>");
                    pw.println("<div id='outer-panel'>");
                    pw.println("<!-- left panel starts here -->");
                    pw.println("<div id='left-panel'>");
                    pw.println("Code here<br>");
                    pw.println("<textarea id='codeArea' name='codeArea' rows='34' cols='130'>" + code + "</textarea>");
                    pw.println("</div> <!-- left panel ends here -->");
                    pw.println("<div id='right-panel'>");
                    pw.println("<div id='input-area'>");
                    pw.println("&nbsp &nbsp Input<br>");
                    pw.println("<textarea id='input-data' name='inputData' rows='14' cols='61'>" + inputData + "</textarea>");
                    pw.println("</div>");
                    pw.println("<div id='output-area'>");
                    pw.println("&nbsp &nbsp Output<br>");
                    pw.println("<textarea id='output-data' name='outputData' rows='17' cols='61' readonly>");
                    while (raf.getFilePointer() < raf.length()) {
                        pw.println(raf.readLine() + "\r\n");
                    }
                    pw.println("</textarea>");
                    pw.println("</div>");
                    pw.println("</div><!-- right panel ends here -->");
                    pw.println("<br>");
                    pw.println("<div id='run-button-panel'>");
                    pw.println("<center>");
                    pw.println("<button type='submit' id='run-button'>Run</button>");
                    pw.println("</center>");
                    pw.println("</div>");
                    pw.println("</div> <!-- content section ends here -->");
                    pw.println("</form>");
                    pw.println("<br>");
                    pw.println("<!-- footer starts here -->");
                    pw.println("<center>");
                    pw.println("<div id='footer'>");
                    pw.println("&copy; LetsCompile 2024");
                    pw.println("</div>");
                    pw.println("</center>");
                    pw.println("<!-- footer ends here -->");
                    pw.println("</div> <!-- Main container ends here -->");
                    pw.println("</body>");
                    pw.println("</html>");
                    raf.close();
                    if(srcFile.exists()) srcFile.delete();
                    if(stderr.exists()) stderr.delete();
                    return;
                }
                raf.close();
                if(srcFile.exists()) srcFile.delete();
                if(stderr.exists()) stderr.delete();
            }
            java.util.List<String> commands = new ArrayList<>();
            if (language.equals("C") || language.equals("C++")) {
                commands.add("C:" + File.separator + "tomcat9" + File.separator + "webapps" + File.separator + "LetsCompile" + File.separator + "tmp" + File.separator + output);
            } else if (language.equals("Java")) {
                commands.add("java");
                commands.add("-classpath");
                commands.add("C:" + File.separator + "tomcat9" + File.separator + "webapps" + File.separator + "LetsCompile" + File.separator + "tmp");
                commands.add("Main");
            } else if (language.equals("Python")) {
                commands.add("py");
                commands.add("C:" + File.separator + "tomcat9" + File.separator + "webapps" + File.separator + "LetsCompile" + File.separator + "tmp" + File.separator + src);
            }
            String input = univString.concat("Input.data");
            File stdin = new File("C:" + File.separator + "tomcat9" + File.separator + "webapps" + File.separator + "LetsCompile" + File.separator + "tmp" + File.separator + input);
            raf = new RandomAccessFile(stdin, "rw");
            raf.writeBytes(inputData);
            raf.close();
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(commands);
            String result = univString.concat("Output.data");
            File stdout = new File("C:" + File.separator + "tomcat9" + File.separator + "webapps" + File.separator + "LetsCompile" + File.separator + "tmp" + File.separator + result);
            pb.redirectOutput(stdout);
            pb.redirectInput(stdin);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            process.waitFor();
            PrintWriter pw = response.getWriter();
            response.setContentType("text/html");
            pw.println("<!DOCTYPE>");
            pw.println("<html lang='en'>");
            pw.println("<head>");
            pw.println("<meta charset='UTF-8'>");
            pw.println("<title>Lets Compile</title>");
            pw.println("<link rel='stylesheet' type='text/css' href='/LetsCompile/css/styles.css'>");
            pw.println("<script src='/LetsCompile/js/LanguageChange.js'></script>");
            pw.println("</head>");
            pw.println("<body>");
            pw.println("<!-- Main conatiner starts here -->");
            pw.println("<div id='main-container'>");
            pw.println("<!-- Header starts here -->");
            pw.println("<div id='header'>");
            pw.println("<img src='/LetsCompile/images/logo.png' width:'15' height:'15' id='logo'>");
            pw.println("<div id='heading'>LetsCompile</div>");
            pw.println("</div> <!-- Header ends here -->");
            pw.println("<!-- content section starts here -->");
            pw.println("<form method='post' action='/LetsCompile/runCode'>");
            pw.println("<div id='content-section'>");
            pw.println("&nbsp;&nbsp;&nbsp;<label for='languages'>Choose a language</label>");
            pw.println("<select id='languages' name='languages' onchange='langChange();'>");
            if (language.equals("C"))
                pw.println("<option selected value='C'>C</option>");
            else
                pw.println("<option value='C'>C</option>");
            if (language.equals("C++"))
                pw.println("<option selected value='C++'>C++</option>");
            else
                pw.println("<option value='C++'>C++</option>");
            if (language.equals("Java"))
                pw.println("<option selected value='Java'>Java</option>");
            else
                pw.println("<option value='Java'>Java</option>");
            if (language.equals("Python"))
                pw.println("<option selected value='Python'>Python</option>");
            else
                pw.println("<option value='Python'>Python</option>");
            pw.println("</select>");
            pw.println("</div>");
            pw.println("<div id='outer-panel'>");
            pw.println("<!-- left panel starts here -->");
            pw.println("<div id='left-panel'>");
            pw.println("Code here<br>");
            pw.println("<textarea id='codeArea' name='codeArea' rows='34' cols='130'>" + code + "</textarea>");
            pw.println("</div> <!-- right panel ends here -->");
            pw.println("<div id='right-panel'>");
            pw.println("<div id='input-area'>");
            pw.println("&nbsp &nbsp Input<br>");
            pw.println("<textarea id='input-data' name='inputData' rows='14' cols='61'>" + inputData + "</textarea>");
            pw.println("</div>");
            pw.println("<div id='output-area'>");
            pw.println("&nbsp &nbsp Output<br>");
            pw.println("<textarea id='output-data' name='outputData' rows='17' cols='61' readonly'>");
            raf = new RandomAccessFile(stdout, "r");
            while (raf.getFilePointer() < raf.length()) {
                pw.println(raf.readLine() + "\r\n");
            }
            raf.close();
            pw.println("</textarea>");
            pw.println("</div>");
            pw.println("</div><!-- right panel ends here -->");
            pw.println("<br>");
            pw.println("<div id='run-button-panel'>");
            pw.println("<center>");
            pw.println("<button type='submit' id='run-button'>Run</button>");
            pw.println("</center>");
            pw.println("</div>");
            pw.println("</div> <!-- content section ends here -->");
            pw.println("</form>");
            pw.println("<br>");
            pw.println("<!-- footer starts here -->");
            pw.println("<center>");
            pw.println("<div id='footer'>");
            pw.println("&copy; LetsCompile 2024");
            pw.println("</div>");
            pw.println("</center>");
            pw.println("<!-- footer ends here -->");
            pw.println("</div> <!-- Main container ends here -->");
            pw.println("</body>");
            pw.println("</html>");
            if(stdin.exists()) stdin.delete();
            if(stdout.exists()) stdout.delete();
            File exeFile=new File("C:" + File.separator + "tomcat9" + File.separator + "webapps" + File.separator + "LetsCompile" + File.separator + "tmp" + File.separator + "Main.class");
            if(exeFile.exists()) exeFile.delete();
            exeFile=new File("C:" + File.separator + "tomcat9" + File.separator + "webapps" + File.separator + "LetsCompile" + File.separator + "tmp" + File.separator + output);
            if(exeFile.exists()) exeFile.delete();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}