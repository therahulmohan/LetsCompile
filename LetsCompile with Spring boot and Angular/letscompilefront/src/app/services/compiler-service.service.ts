import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CodeSnippet } from '../classes/code-snippet';

@Injectable({
  providedIn: 'root'
})
export class CompilerServiceService {

  constructor(private httpClient:HttpClient) { }
  runCode(codeSnippet : CodeSnippet) {
    return this.httpClient.post<String>("http://localhost:8080/compile",codeSnippet);
  }
}
