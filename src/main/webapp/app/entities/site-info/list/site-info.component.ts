import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISiteInfo } from '../site-info.model';
import { SiteInfoService } from '../service/site-info.service';
import { SiteInfoDeleteDialogComponent } from '../delete/site-info-delete-dialog.component';

@Component({
  selector: 'jhi-site-info',
  templateUrl: './site-info.component.html',
})
export class SiteInfoComponent implements OnInit {
  siteInfos?: ISiteInfo[];
  isLoading = false;

  constructor(protected siteInfoService: SiteInfoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.siteInfoService.query().subscribe({
      next: (res: HttpResponse<ISiteInfo[]>) => {
        this.isLoading = false;
        this.siteInfos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISiteInfo): number {
    return item.id!;
  }

  delete(siteInfo: ISiteInfo): void {
    const modalRef = this.modalService.open(SiteInfoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.siteInfo = siteInfo;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
