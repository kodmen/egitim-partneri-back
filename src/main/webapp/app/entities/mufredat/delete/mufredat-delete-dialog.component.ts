import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMufredat } from '../mufredat.model';
import { MufredatService } from '../service/mufredat.service';

@Component({
  templateUrl: './mufredat-delete-dialog.component.html',
})
export class MufredatDeleteDialogComponent {
  mufredat?: IMufredat;

  constructor(protected mufredatService: MufredatService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mufredatService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
