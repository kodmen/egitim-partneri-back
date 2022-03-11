import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMufredat } from '../mufredat.model';
import { MufredatService } from '../service/mufredat.service';
import { MufredatDeleteDialogComponent } from '../delete/mufredat-delete-dialog.component';

@Component({
  selector: 'jhi-mufredat',
  templateUrl: './mufredat.component.html',
})
export class MufredatComponent implements OnInit {
  mufredats?: IMufredat[];
  isLoading = false;

  constructor(protected mufredatService: MufredatService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.mufredatService.query().subscribe({
      next: (res: HttpResponse<IMufredat[]>) => {
        this.isLoading = false;
        this.mufredats = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IMufredat): number {
    return item.id!;
  }

  delete(mufredat: IMufredat): void {
    const modalRef = this.modalService.open(MufredatDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.mufredat = mufredat;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
