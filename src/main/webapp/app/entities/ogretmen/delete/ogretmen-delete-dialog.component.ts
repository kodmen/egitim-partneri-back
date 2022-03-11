import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOgretmen } from '../ogretmen.model';
import { OgretmenService } from '../service/ogretmen.service';

@Component({
  templateUrl: './ogretmen-delete-dialog.component.html',
})
export class OgretmenDeleteDialogComponent {
  ogretmen?: IOgretmen;

  constructor(protected ogretmenService: OgretmenService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ogretmenService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
