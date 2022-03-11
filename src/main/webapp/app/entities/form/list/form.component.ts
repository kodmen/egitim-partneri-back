import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IForm } from '../form.model';
import { FormService } from '../service/form.service';
import { FormDeleteDialogComponent } from '../delete/form-delete-dialog.component';

@Component({
  selector: 'jhi-form',
  templateUrl: './form.component.html',
})
export class FormComponent implements OnInit {
  forms?: IForm[];
  isLoading = false;

  constructor(protected formService: FormService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.formService.query().subscribe({
      next: (res: HttpResponse<IForm[]>) => {
        this.isLoading = false;
        this.forms = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IForm): number {
    return item.id!;
  }

  delete(form: IForm): void {
    const modalRef = this.modalService.open(FormDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.form = form;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
