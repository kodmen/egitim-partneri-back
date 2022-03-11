import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRozet, Rozet } from '../rozet.model';
import { RozetService } from '../service/rozet.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-rozet-update',
  templateUrl: './rozet-update.component.html',
})
export class RozetUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    rozetIsmi: [null, [Validators.maxLength(500)]],
    rozetResim: [],
    rozetResimContentType: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected rozetService: RozetService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rozet }) => {
      this.updateForm(rozet);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('gamificationApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rozet = this.createFromForm();
    if (rozet.id !== undefined) {
      this.subscribeToSaveResponse(this.rozetService.update(rozet));
    } else {
      this.subscribeToSaveResponse(this.rozetService.create(rozet));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRozet>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(rozet: IRozet): void {
    this.editForm.patchValue({
      id: rozet.id,
      rozetIsmi: rozet.rozetIsmi,
      rozetResim: rozet.rozetResim,
      rozetResimContentType: rozet.rozetResimContentType,
    });
  }

  protected createFromForm(): IRozet {
    return {
      ...new Rozet(),
      id: this.editForm.get(['id'])!.value,
      rozetIsmi: this.editForm.get(['rozetIsmi'])!.value,
      rozetResimContentType: this.editForm.get(['rozetResimContentType'])!.value,
      rozetResim: this.editForm.get(['rozetResim'])!.value,
    };
  }
}
