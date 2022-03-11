import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDers } from '../ders.model';
import { DersService } from '../service/ders.service';
import { DersDeleteDialogComponent } from '../delete/ders-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-ders',
  templateUrl: './ders.component.html',
})
export class DersComponent implements OnInit {
  ders?: IDers[];
  isLoading = false;

  constructor(protected dersService: DersService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.dersService.query().subscribe({
      next: (res: HttpResponse<IDers[]>) => {
        this.isLoading = false;
        this.ders = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDers): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(ders: IDers): void {
    const modalRef = this.modalService.open(DersDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ders = ders;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
