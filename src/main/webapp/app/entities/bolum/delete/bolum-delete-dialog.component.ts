import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBolum } from '../bolum.model';
import { BolumService } from '../service/bolum.service';

@Component({
  templateUrl: './bolum-delete-dialog.component.html',
})
export class BolumDeleteDialogComponent {
  bolum?: IBolum;

  constructor(protected bolumService: BolumService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bolumService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
