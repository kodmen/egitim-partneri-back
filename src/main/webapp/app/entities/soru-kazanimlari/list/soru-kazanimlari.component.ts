import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISoruKazanimlari } from '../soru-kazanimlari.model';
import { SoruKazanimlariService } from '../service/soru-kazanimlari.service';
import { SoruKazanimlariDeleteDialogComponent } from '../delete/soru-kazanimlari-delete-dialog.component';

@Component({
  selector: 'jhi-soru-kazanimlari',
  templateUrl: './soru-kazanimlari.component.html',
})
export class SoruKazanimlariComponent implements OnInit {
  soruKazanimlaris?: ISoruKazanimlari[];
  isLoading = false;

  constructor(protected soruKazanimlariService: SoruKazanimlariService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.soruKazanimlariService.query().subscribe({
      next: (res: HttpResponse<ISoruKazanimlari[]>) => {
        this.isLoading = false;
        this.soruKazanimlaris = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISoruKazanimlari): number {
    return item.id!;
  }

  delete(soruKazanimlari: ISoruKazanimlari): void {
    const modalRef = this.modalService.open(SoruKazanimlariDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.soruKazanimlari = soruKazanimlari;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
