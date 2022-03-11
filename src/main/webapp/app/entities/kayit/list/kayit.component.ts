import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IKayit } from '../kayit.model';
import { KayitService } from '../service/kayit.service';
import { KayitDeleteDialogComponent } from '../delete/kayit-delete-dialog.component';

@Component({
  selector: 'jhi-kayit',
  templateUrl: './kayit.component.html',
})
export class KayitComponent implements OnInit {
  kayits?: IKayit[];
  isLoading = false;

  constructor(protected kayitService: KayitService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.kayitService.query().subscribe({
      next: (res: HttpResponse<IKayit[]>) => {
        this.isLoading = false;
        this.kayits = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IKayit): number {
    return item.id!;
  }

  delete(kayit: IKayit): void {
    const modalRef = this.modalService.open(KayitDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.kayit = kayit;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
