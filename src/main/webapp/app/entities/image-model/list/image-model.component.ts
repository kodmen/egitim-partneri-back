import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IImageModel } from '../image-model.model';
import { ImageModelService } from '../service/image-model.service';
import { ImageModelDeleteDialogComponent } from '../delete/image-model-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-image-model',
  templateUrl: './image-model.component.html',
})
export class ImageModelComponent implements OnInit {
  imageModels?: IImageModel[];
  isLoading = false;

  constructor(protected imageModelService: ImageModelService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.imageModelService.query().subscribe({
      next: (res: HttpResponse<IImageModel[]>) => {
        this.isLoading = false;
        this.imageModels = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IImageModel): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(imageModel: IImageModel): void {
    const modalRef = this.modalService.open(ImageModelDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.imageModel = imageModel;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
