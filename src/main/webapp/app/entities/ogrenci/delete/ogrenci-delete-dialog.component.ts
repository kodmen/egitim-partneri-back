import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOgrenci } from '../ogrenci.model';
import { OgrenciService } from '../service/ogrenci.service';

@Component({
  templateUrl: './ogrenci-delete-dialog.component.html',
})
export class OgrenciDeleteDialogComponent {
  ogrenci?: IOgrenci;

  constructor(protected ogrenciService: OgrenciService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ogrenciService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
