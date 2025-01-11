import { CommonModule } from '@angular/common'; // Import CommonModule
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import * as ace from 'ace-builds';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FormsModule, CommonModule], // Add CommonModule here
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  title = 'Online Code Editor';
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
