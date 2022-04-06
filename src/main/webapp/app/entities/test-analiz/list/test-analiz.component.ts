import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITestAnaliz } from '../test-analiz.model';
import { TestAnalizService } from '../service/test-analiz.service';
import { TestAnalizDeleteDialogComponent } from '../delete/test-analiz-delete-dialog.component';

@Component({
  selector: 'jhi-test-analiz',
  templateUrl: './test-analiz.component.html',
})
export class TestAnalizComponent implements OnInit {
  testAnalizs?: ITestAnaliz[];
  isLoading = false;

  constructor(protected testAnalizService: TestAnalizService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.testAnalizService.query().subscribe({
      next: (res: HttpResponse<ITestAnaliz[]>) => {
        this.isLoading = false;
        this.testAnalizs = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITestAnaliz): number {
    return item.id!;
  }

  delete(testAnaliz: ITestAnaliz): void {
    const modalRef = this.modalService.open(TestAnalizDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.testAnaliz = testAnaliz;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
