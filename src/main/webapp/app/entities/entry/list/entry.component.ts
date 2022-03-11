import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEntry } from '../entry.model';
import { EntryService } from '../service/entry.service';
import { EntryDeleteDialogComponent } from '../delete/entry-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-entry',
  templateUrl: './entry.component.html',
})
export class EntryComponent implements OnInit {
  entries?: IEntry[];
  isLoading = false;

  constructor(protected entryService: EntryService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.entryService.query().subscribe({
      next: (res: HttpResponse<IEntry[]>) => {
        this.isLoading = false;
        this.entries = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IEntry): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(entry: IEntry): void {
    const modalRef = this.modalService.open(EntryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.entry = entry;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
