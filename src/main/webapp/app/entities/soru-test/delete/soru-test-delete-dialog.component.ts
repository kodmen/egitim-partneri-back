import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISoruTest } from '../soru-test.model';
import { SoruTestService } from '../service/soru-test.service';

@Component({
  templateUrl: './soru-test-delete-dialog.component.html',
})
export class SoruTestDeleteDialogComponent {
  soruTest?: ISoruTest;

  constructor(protected soruTestService: SoruTestService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.soruTestService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
