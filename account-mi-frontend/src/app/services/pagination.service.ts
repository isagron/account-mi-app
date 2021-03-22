import {PageIndex} from '../models/utils.model';
import {BehaviorSubject} from 'rxjs';


export interface PaginationData {
  pages: PageIndex[];
  disablePrev: boolean;
  disableNext: boolean;
}
export class PaginationService {

  paginationSubject = new BehaviorSubject<PaginationData>({pages: [], disablePrev: true, disableNext: true}) ;

  setPageInformation(pageNumber: number, pageSize: number, totalElements: number) {
    if (totalElements > 0) {
      const numberOfPages = Math.floor((totalElements - 1) / pageSize);
      // only single page
      if (numberOfPages === 0) {
        const currentPageIndex = new PageIndex(0, pageNumber, true);
        this.paginationSubject.next({pages: [currentPageIndex],
          disablePrev: true,
          disableNext: true});
        return;
      }

      // 2 pages
      if (numberOfPages === 1) {
        if (pageNumber === 0) {
          const currentPageIndex = new PageIndex(0, pageNumber, true);
          const nextPageIndex = new PageIndex(1, pageNumber + 1, false);
          this.paginationSubject.next({pages: [currentPageIndex, nextPageIndex],
            disablePrev: true,
            disableNext: false});
        } else {
          const prevPageIndex = new PageIndex(0, pageNumber - 1, false);
          const currentPageIndex = new PageIndex(1, pageNumber, true);
          this.paginationSubject.next({pages: [prevPageIndex, currentPageIndex],
            disablePrev: false,
            disableNext: true});
        }
        return;
      }

      // more than 2 pages
      if (numberOfPages > 1) {
        if (pageNumber === 0) {
          const prevPageIndex = new PageIndex(0, pageNumber, true);
          const currentPageIndex = new PageIndex(1, pageNumber + 1, false);
          const nextPageIndex = new PageIndex(2, pageNumber + 2, false);
          this.paginationSubject.next({pages: [prevPageIndex, currentPageIndex, nextPageIndex],
            disablePrev: true,
            disableNext: false});
        } else {
          if (pageNumber === numberOfPages) {
            const prevPageIndex = new PageIndex(0, pageNumber - 2, false);
            const currentPageIndex = new PageIndex(1, pageNumber - 1 , false);
            const nextPageIndex = new PageIndex(2, pageNumber, true);
            this.paginationSubject.next({pages: [prevPageIndex, currentPageIndex, nextPageIndex],
              disablePrev: false,
              disableNext: true});
          } else {
            const prevPageIndex = new PageIndex(0, pageNumber - 1, false);
            const currentPageIndex = new PageIndex(1, pageNumber, true);
            const nextPageIndex = new PageIndex(2, pageNumber + 1, false);
            this.paginationSubject.next({pages: [prevPageIndex, currentPageIndex, nextPageIndex],
              disablePrev: false,
              disableNext: false});
          }
        }
        return;
      }
    } else {
      this.paginationSubject.next({pages: [],
        disablePrev: true,
        disableNext: true});
    }
  }
}
