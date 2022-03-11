import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IKayit } from '../kayit.model';
import { KayitService } from '../service/kayit.service';

@Component({
  templateUrl: './kayit-delete-dialog.component.html',
})
export class KayitDeleteDialogComponent {
  kayit?: IKayit;

  constructor(protected kayitService: KayitService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.kayitService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
