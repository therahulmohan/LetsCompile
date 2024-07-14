package com.lets.compile.letscompileback.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lets.compile.letscompileback.entities.CodeSnippet;
import com.lets.compile.letscompileback.services.CompilerServiceForC;
import com.lets.compile.letscompileback.services.CompilerServiceForCPP;
import com.lets.compile.letscompileback.services.CompilerServiceForJava;
import com.lets.compile.letscompileback.services.CompilerServiceForPython;

@RestController
@CrossOrigin("*")
public class CompilerController {
    @Autowired
    private CompilerServiceForC compileServiceForC;
    @Autowired
    private CompilerServiceForCPP compileServiceForCPP;
    @Autowired
    private CompilerServiceForJava compilerServiceForJava;
    @Autowired
    private CompilerServiceForPython compilerServiceForPython;

    public CompilerController() {
        
    }

    @PostMapping("/compile")
    public String complie(@RequestBody CodeSnippet codeSnippet) throws IOException, InterruptedException {
        String code=codeSnippet.getCode();
        String input=codeSnippet.getInput();
        String language=codeSnippet.getLanguage();
        String output="";
        if(language.equals("C")) {
            output=this.compileServiceForC.compileCode(code, input);
        }
        else if(language.equals("C++")) {
            output=this.compileServiceForCPP.compileCode(code, input);
        }
        else if(language.equals("Java")) {
            output=this.compilerServiceForJava.compileCode(code, input);
        }
        else if(language.equals("Python")) {
            output=this.compilerServiceForPython.compileCode(code, input);
        }
        return output;
    }
}
