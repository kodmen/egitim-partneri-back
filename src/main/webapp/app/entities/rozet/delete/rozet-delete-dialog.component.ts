import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRozet } from '../rozet.model';
import { RozetService } from '../service/rozet.service';

@Component({
  templateUrl: './rozet-delete-dialog.component.html',
})
export class RozetDeleteDialogComponent {
  rozet?: IRozet;

  constructor(protected rozetService: RozetService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rozetService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
