import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IBolum, Bolum } from '../bolum.model';
import { BolumService } from '../service/bolum.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-bolum-update',
  templateUrl: './bolum-update.component.html',
})
export class BolumUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    bolumBaslik: [null, [Validators.maxLength(500)]],
    dokuman: [],
    puan: [],
    videoLink: [null, [Validators.maxLength(1000)]],
    sure: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected bolumService: BolumService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bolum }) => {
      this.updateForm(bolum);
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

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bolum = this.createFromForm();
    if (bolum.id !== undefined) {
      this.subscribeToSaveResponse(this.bolumService.update(bolum));
    } else {
      this.subscribeToSaveResponse(this.bolumService.create(bolum));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBolum>>): void {
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

  protected updateForm(bolum: IBolum): void {
    this.editForm.patchValue({
      id: bolum.id,
      bolumBaslik: bolum.bolumBaslik,
      dokuman: bolum.dokuman,
      puan: bolum.puan,
      videoLink: bolum.videoLink,
      sure: bolum.sure,
    });
  }

  protected createFromForm(): IBolum {
    return {
      ...new Bolum(),
      id: this.editForm.get(['id'])!.value,
      bolumBaslik: this.editForm.get(['bolumBaslik'])!.value,
      dokuman: this.editForm.get(['dokuman'])!.value,
      puan: this.editForm.get(['puan'])!.value,
      videoLink: this.editForm.get(['videoLink'])!.value,
      sure: this.editForm.get(['sure'])!.value,
    };
  }
}
