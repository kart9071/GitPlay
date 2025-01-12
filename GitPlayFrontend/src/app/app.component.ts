import { Component, OnInit } from '@angular/core';
import { CompilerComponent } from "./Controller/compiler/compiler.component";


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CompilerComponent], // Add CommonModule here
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  title="GitPlay"
  ngOnInit(): void {

  }
}
