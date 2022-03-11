import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBolum } from '../bolum.model';
import { BolumService } from '../service/bolum.service';
import { BolumDeleteDialogComponent } from '../delete/bolum-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-bolum',
  templateUrl: './bolum.component.html',
})
export class BolumComponent implements OnInit {
  bolums?: IBolum[];
  isLoading = false;

  constructor(protected bolumService: BolumService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.bolumService.query().subscribe({
      next: (res: HttpResponse<IBolum[]>) => {
        this.isLoading = false;
        this.bolums = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IBolum): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(bolum: IBolum): void {
    const modalRef = this.modalService.open(BolumDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.bolum = bolum;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
