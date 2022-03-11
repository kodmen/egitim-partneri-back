import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IYorum } from '../yorum.model';
import { YorumService } from '../service/yorum.service';

@Component({
  templateUrl: './yorum-delete-dialog.component.html',
})
export class YorumDeleteDialogComponent {
  yorum?: IYorum;

  constructor(protected yorumService: YorumService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.yorumService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
