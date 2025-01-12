import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import * as ace from 'ace-builds';

@Component({
  selector: 'app-compiler',
  imports: [FormsModule,CommonModule],
  templateUrl: './compiler.component.html',
  styleUrl: './compiler.component.css'
})
export class CompilerComponent {
   title='Compiler Page';
   editor: any;
    selectedLanguage = 'javascript';
    languages = [
      { name: 'JavaScript', value: 'javascript' },
      { name: 'Python', value: 'python' },
      { name: 'Java', value: 'java' },
      { name: 'C++', value: 'cpp' },
    ];
  
    constructor(private http: HttpClient) {}
  
    ngOnInit(): void {
      this.editor = ace.edit('editor');
      this.editor.setTheme('ace/theme/monokai');
      this.editor.session.setMode('ace/mode/javascript');
      const cursorElement = this.editor.container.querySelector('.ace_cursor');
      if (cursorElement) {
        cursorElement.style.borderLeft = '2px solid white'; // White cursor color
      }
      this.editor.renderer.setShowGutter(true);
      this.editor.session.insert({ row: 0, column: 0 }, "\n");
      this.editor.container.style.setProperty('--cursor-blink', '1s step-end infinite');
      this.editor.renderer.updateFull();
    }
  
    compileCode(): void {
      const code = this.editor.getValue();
      const payload = { code, language: this.selectedLanguage };
  
      this.http.post('http://localhost:8081/compile', payload).subscribe({
        next: (response) => console.log('Output:', response),
        error: (err) => console.error('Error:', err),
      });
    }
}
