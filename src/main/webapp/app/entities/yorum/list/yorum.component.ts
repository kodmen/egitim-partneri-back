import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IYorum } from '../yorum.model';
import { YorumService } from '../service/yorum.service';
import { YorumDeleteDialogComponent } from '../delete/yorum-delete-dialog.component';

@Component({
  selector: 'jhi-yorum',
  templateUrl: './yorum.component.html',
})
export class YorumComponent implements OnInit {
  yorums?: IYorum[];
  isLoading = false;

  constructor(protected yorumService: YorumService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.yorumService.query().subscribe({
      next: (res: HttpResponse<IYorum[]>) => {
        this.isLoading = false;
        this.yorums = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IYorum): number {
    return item.id!;
  }

  delete(yorum: IYorum): void {
    const modalRef = this.modalService.open(YorumDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.yorum = yorum;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
