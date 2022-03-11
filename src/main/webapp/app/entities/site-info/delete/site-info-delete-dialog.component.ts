import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISiteInfo } from '../site-info.model';
import { SiteInfoService } from '../service/site-info.service';

@Component({
  templateUrl: './site-info-delete-dialog.component.html',
})
export class SiteInfoDeleteDialogComponent {
  siteInfo?: ISiteInfo;

  constructor(protected siteInfoService: SiteInfoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.siteInfoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
