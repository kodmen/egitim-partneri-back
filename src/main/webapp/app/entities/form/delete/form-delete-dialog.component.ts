import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IForm } from '../form.model';
import { FormService } from '../service/form.service';

@Component({
  templateUrl: './form-delete-dialog.component.html',
})
export class FormDeleteDialogComponent {
  form?: IForm;

  constructor(protected formService: FormService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.formService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
