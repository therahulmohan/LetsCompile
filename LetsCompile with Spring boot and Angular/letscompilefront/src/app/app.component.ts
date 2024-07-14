import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterOutlet } from '@angular/router';
import { CodeSnippet } from './classes/code-snippet';
import { CompilerServiceService } from './services/compiler-service.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet,FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {

  title = 'letscompilefront';
  language: String="C"
  code: any
  codeForC: String = "#include<stdio.h>\n\nint main ()\n{\nprintf(\"Hello World!\");\nreturn 0;\n}"
  codeForCPP: String = "#include<iostream>\nusing namespace std;\n\nint main ()\n{\ncout<<\"Hello World!\";\nreturn 0;\n}"
  codeForJava: String = "class Main{\npublic static void main(String args[]) {\nSystem.out.println(\"Hello World!\");\n}\n}"
  codeForPython: String = "print(\"Hello World!\")"
  output : any
  input : String=""
  codeSnippet : CodeSnippet | undefined

  constructor(private compilerService:CompilerServiceService) {}

  onChange(event : Event) {
    this.language=(event.target as HTMLSelectElement).value
    if (this.language == "C") {
      this.code = this.codeForC
    }
    else if (this.language == "C++") {
      this.code = this.codeForCPP
    }
    else if (this.language == "Java") {
      this.code = this.codeForJava
    }
    else if (this.language == "Python") {
      this.code = this.codeForPython
    }
  }

  ngOnInit(): void {
    this.code=this.codeForC
  }

  runCode() {
    this.codeSnippet=new CodeSnippet();
    this.codeSnippet.code=this.code
    this.codeSnippet.language=this.language
    this.codeSnippet.input=this.input
    this.compilerService?.runCode(this.codeSnippet).subscribe({
      next: data => {
        this.output=data
      },
      error: error => {
        this.output=error.error.text
      }
    });
  }
}
