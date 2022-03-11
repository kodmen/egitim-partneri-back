import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRozet } from '../rozet.model';
import { RozetService } from '../service/rozet.service';
import { RozetDeleteDialogComponent } from '../delete/rozet-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-rozet',
  templateUrl: './rozet.component.html',
})
export class RozetComponent implements OnInit {
  rozets?: IRozet[];
  isLoading = false;

  constructor(protected rozetService: RozetService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.rozetService.query().subscribe({
      next: (res: HttpResponse<IRozet[]>) => {
        this.isLoading = false;
        this.rozets = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IRozet): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(rozet: IRozet): void {
    const modalRef = this.modalService.open(RozetDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.rozet = rozet;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
