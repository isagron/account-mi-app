export class CommonValues {
  static months: string[] = [
    'January',
    'February',
    'March',
    'April',
    'May',
    'June',
    'July',
    'August',
    'September',
    'October',
    'November',
    'December'];
}

export class PageIndex {
  constructor(public index: number, public label: number, public active: boolean) {
  }
}

export class Page {
  constructor(public page: number, public size: number) {
  }
}
