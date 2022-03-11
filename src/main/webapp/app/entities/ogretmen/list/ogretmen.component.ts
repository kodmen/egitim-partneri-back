import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOgretmen } from '../ogretmen.model';
import { OgretmenService } from '../service/ogretmen.service';
import { OgretmenDeleteDialogComponent } from '../delete/ogretmen-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-ogretmen',
  templateUrl: './ogretmen.component.html',
})
export class OgretmenComponent implements OnInit {
  ogretmen?: IOgretmen[];
  isLoading = false;

  constructor(protected ogretmenService: OgretmenService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.ogretmenService.query().subscribe({
      next: (res: HttpResponse<IOgretmen[]>) => {
        this.isLoading = false;
        this.ogretmen = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IOgretmen): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(ogretmen: IOgretmen): void {
    const modalRef = this.modalService.open(OgretmenDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ogretmen = ogretmen;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
