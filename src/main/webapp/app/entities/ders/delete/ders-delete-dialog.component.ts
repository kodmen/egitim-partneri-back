import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDers } from '../ders.model';
import { DersService } from '../service/ders.service';

@Component({
  templateUrl: './ders-delete-dialog.component.html',
})
export class DersDeleteDialogComponent {
  ders?: IDers;

  constructor(protected dersService: DersService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dersService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
