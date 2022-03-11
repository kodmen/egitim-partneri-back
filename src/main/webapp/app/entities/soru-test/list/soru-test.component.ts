import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISoruTest } from '../soru-test.model';
import { SoruTestService } from '../service/soru-test.service';
import { SoruTestDeleteDialogComponent } from '../delete/soru-test-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-soru-test',
  templateUrl: './soru-test.component.html',
})
export class SoruTestComponent implements OnInit {
  soruTests?: ISoruTest[];
  isLoading = false;

  constructor(protected soruTestService: SoruTestService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.soruTestService.query().subscribe({
      next: (res: HttpResponse<ISoruTest[]>) => {
        this.isLoading = false;
        this.soruTests = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISoruTest): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(soruTest: ISoruTest): void {
    const modalRef = this.modalService.open(SoruTestDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.soruTest = soruTest;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
