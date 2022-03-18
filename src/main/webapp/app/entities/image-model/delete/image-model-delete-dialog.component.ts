import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IImageModel } from '../image-model.model';
import { ImageModelService } from '../service/image-model.service';

@Component({
  templateUrl: './image-model-delete-dialog.component.html',
})
export class ImageModelDeleteDialogComponent {
  imageModel?: IImageModel;

  constructor(protected imageModelService: ImageModelService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.imageModelService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
