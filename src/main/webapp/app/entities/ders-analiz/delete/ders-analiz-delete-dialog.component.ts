import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDersAnaliz } from '../ders-analiz.model';
import { DersAnalizService } from '../service/ders-analiz.service';

@Component({
  templateUrl: './ders-analiz-delete-dialog.component.html',
})
export class DersAnalizDeleteDialogComponent {
  dersAnaliz?: IDersAnaliz;

  constructor(protected dersAnalizService: DersAnalizService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dersAnalizService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
