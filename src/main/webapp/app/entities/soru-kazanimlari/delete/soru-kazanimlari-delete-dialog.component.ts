import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISoruKazanimlari } from '../soru-kazanimlari.model';
import { SoruKazanimlariService } from '../service/soru-kazanimlari.service';

@Component({
  templateUrl: './soru-kazanimlari-delete-dialog.component.html',
})
export class SoruKazanimlariDeleteDialogComponent {
  soruKazanimlari?: ISoruKazanimlari;

  constructor(protected soruKazanimlariService: SoruKazanimlariService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.soruKazanimlariService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
