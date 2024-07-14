function langChange() {
    var languages = document.getElementById("languages");
    var language = languages.options[languages.selectedIndex].value;
    var codeArea = document.getElementById("codeArea");
    var inputArea = document.getElementById("input-data");
    var outputArea = document.getElementById("output-data");
    if (language.localeCompare("C") == 0) {
        inputArea.value = '';
        outputArea.value = '';
        codeArea.value = '';
        codeArea.value += '#include<stdio.h>\n\n';
        codeArea.value += 'int main () {\n';
        codeArea.value += 'printf("Hello World!");\n';
        codeArea.value += 'return 0;\n';
        codeArea.value += '}';
    }
    else if (language.localeCompare("C++") == 0) {
        inputArea.value = '';
        outputArea.value = '';
        codeArea.value = '';
        codeArea.value += '#include<iostream>\n';
        codeArea.value += 'using namespace std;\n\n';
        codeArea.value += 'int main () {\n';
        codeArea.value += 'cout<<"Hello World!";\n';
        codeArea.value += 'return 0;\n';
        codeArea.value += '}';
    }
    else if (language.localeCompare("Java") == 0) {
        inputArea.value = '';
        outputArea.value = '';
        codeArea.value = '';
        codeArea.value += 'class Main{\n';
        codeArea.value += 'public static void main(String args[]) {\n';
        codeArea.value += 'System.out.println("Hello World!");\n';
        codeArea.value += '}\n';
        codeArea.value += '}';
    }
    else {
        inputArea.value = '';
        outputArea.value = '';
        codeArea.value = '';
        codeArea.value += 'print("Hello World!")';
    }
}