import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Page, PageIndex} from '../../../models/utils.model';
import {PaginationService} from '../../../services/pagination.service';

@Component({
  selector: 'app-pageination',
  templateUrl: './pageination.component.html',
  styleUrls: ['./pageination.component.css']
})
export class PageinationComponent implements OnInit {

  disablePrev = true;
  disableNext = true;
  pages: PageIndex[] = [];

  @Output() goToPageEvent = new EventEmitter<number>();

  constructor(private paginationService: PaginationService) {
    paginationService.paginationSubject.subscribe(pageData => {
      this.disableNext = pageData.disableNext;
      this.disablePrev = pageData.disablePrev;
      this.pages = pageData.pages;
    });
  }

  ngOnInit(): void {
  }

  goToPrevPage() {
    const currentActivePageIndex = this.pages.findIndex(page => page.active);
    const currentActivePage = this.pages[currentActivePageIndex];
    this.goToPageEvent.emit(currentActivePage.label - 1);
  }

  goToNextPage() {
    const currentActivePageIndex = this.pages.findIndex(page => page.active);
    const currentActivePage = this.pages[currentActivePageIndex];
    this.goToPageEvent.emit(currentActivePage.label + 1);
  }

  goToPage(pageNumberLabel: number) {
    this.goToPageEvent.emit(pageNumberLabel);
  }

}
