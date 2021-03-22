import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-input-tags',
  templateUrl: './input-tags.component.html',
  styleUrls: ['./input-tags.component.css']
})
export class InputTagsComponent implements OnInit {

  @Input() label: string;
  @Output() onAddNewTag = new EventEmitter<string>();
  @Output() onDeleteTag = new EventEmitter<string>();

  @Input() tags: string[] = [];

  constructor() { }

  ngOnInit(): void {
    console.log(this.tags);
  }

  addNewTag(value: string) {
    if (!this.tags.includes(value)) {
      this.tags.push(value);
      this.onAddNewTag.emit(value);
    }
  }

  deleteTag(value: string) {
    if (this.tags.indexOf(value)) {
      this.tags = this.tags.filter(tag => tag !== value);
      this.onDeleteTag.emit(value);
    }
  }
}
