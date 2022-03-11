import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDersAnaliz } from '../ders-analiz.model';
import { DersAnalizService } from '../service/ders-analiz.service';
import { DersAnalizDeleteDialogComponent } from '../delete/ders-analiz-delete-dialog.component';

@Component({
  selector: 'jhi-ders-analiz',
  templateUrl: './ders-analiz.component.html',
})
export class DersAnalizComponent implements OnInit {
  dersAnalizs?: IDersAnaliz[];
  isLoading = false;

  constructor(protected dersAnalizService: DersAnalizService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.dersAnalizService.query().subscribe({
      next: (res: HttpResponse<IDersAnaliz[]>) => {
        this.isLoading = false;
        this.dersAnalizs = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDersAnaliz): number {
    return item.id!;
  }

  delete(dersAnaliz: IDersAnaliz): void {
    const modalRef = this.modalService.open(DersAnalizDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.dersAnaliz = dersAnaliz;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
