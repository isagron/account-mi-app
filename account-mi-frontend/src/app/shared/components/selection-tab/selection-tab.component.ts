import {Component, EventEmitter, Input, OnChanges, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-selection-tab',
  templateUrl: './selection-tab.component.html',
  styleUrls: ['./selection-tab.component.css']
})
export class SelectionTabComponent implements OnInit, OnChanges {

  @Input() options: Array<string>;
  @Input() defaultOptionIndex: number;

  trackedOptions: Array<{ label: string, isActive: boolean }>;

  @Output() optionSelectedEvent = new EventEmitter();

  constructor() {
  }

  ngOnInit(): void {
    this.init();
  }

  ngOnChanges() {
    this.init();
  }

  init() {
    this.trackedOptions = this.options.map(item => Object.create({label: item, isActive: false}));
    if (this.defaultOptionIndex >= 0) {
      this.trackedOptions[this.defaultOptionIndex].isActive = true;
    }
  }

  selectOption(selectedOption: { label: string; isActive: boolean }) {
    this.trackedOptions.forEach(op => op.isActive = false);
    selectedOption.isActive = true;
    this.optionSelectedEvent.emit(selectedOption.label);
  }
}
