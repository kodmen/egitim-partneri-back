import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITestAnaliz } from '../test-analiz.model';
import { TestAnalizService } from '../service/test-analiz.service';

@Component({
  templateUrl: './test-analiz-delete-dialog.component.html',
})
export class TestAnalizDeleteDialogComponent {
  testAnaliz?: ITestAnaliz;

  constructor(protected testAnalizService: TestAnalizService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.testAnalizService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
