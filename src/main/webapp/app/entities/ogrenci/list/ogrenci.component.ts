import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOgrenci } from '../ogrenci.model';
import { OgrenciService } from '../service/ogrenci.service';
import { OgrenciDeleteDialogComponent } from '../delete/ogrenci-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-ogrenci',
  templateUrl: './ogrenci.component.html',
})
export class OgrenciComponent implements OnInit {
  ogrencis?: IOgrenci[];
  isLoading = false;

  constructor(protected ogrenciService: OgrenciService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.ogrenciService.query().subscribe({
      next: (res: HttpResponse<IOgrenci[]>) => {
        this.isLoading = false;
        this.ogrencis = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IOgrenci): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(ogrenci: IOgrenci): void {
    const modalRef = this.modalService.open(OgrenciDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ogrenci = ogrenci;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
