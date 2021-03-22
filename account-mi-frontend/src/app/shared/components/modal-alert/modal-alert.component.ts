import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';


@Component({
  selector: 'app-modal-alert',
  templateUrl: './modal-alert.component.html',
  styleUrls: ['./modal-alert.component.css']
})
export class ModalAlertComponent implements OnInit {

  public static MODAL_CANCEL_CLOSE = 'CANCEL';
  public static MODAL_APPROVE_CLOSE = 'APPROVE';

  modalCancelClose = ModalAlertComponent.MODAL_CANCEL_CLOSE;
  modalApproveClose = ModalAlertComponent.MODAL_APPROVE_CLOSE;

  @Input() modalTitle = '';
  @Input() modalContent = '';
  @Input() okLabel = 'Ok';
  @Input() closeLabel = 'Cancel';


  constructor(public activeModal: NgbActiveModal) {
  }

  ngOnInit(): void {
  }


}
