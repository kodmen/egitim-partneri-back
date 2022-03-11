import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMufredat, Mufredat } from '../mufredat.model';
import { MufredatService } from '../service/mufredat.service';
import { IBolum } from 'app/entities/bolum/bolum.model';
import { BolumService } from 'app/entities/bolum/service/bolum.service';

@Component({
  selector: 'jhi-mufredat-update',
  templateUrl: './mufredat-update.component.html',
})
export class MufredatUpdateComponent implements OnInit {
  isSaving = false;

  bolumsSharedCollection: IBolum[] = [];

  editForm = this.fb.group({
    id: [],
    mufredatBaslik: [null, [Validators.maxLength(500)]],
    toplamSure: [],
    bolumSayi: [],
    bolumlers: [],
  });

  constructor(
    protected mufredatService: MufredatService,
    protected bolumService: BolumService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mufredat }) => {
      this.updateForm(mufredat);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mufredat = this.createFromForm();
    if (mufredat.id !== undefined) {
      this.subscribeToSaveResponse(this.mufredatService.update(mufredat));
    } else {
      this.subscribeToSaveResponse(this.mufredatService.create(mufredat));
    }
  }

  trackBolumById(index: number, item: IBolum): number {
    return item.id!;
  }

  getSelectedBolum(option: IBolum, selectedVals?: IBolum[]): IBolum {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMufredat>>): void {
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

  protected updateForm(mufredat: IMufredat): void {
    this.editForm.patchValue({
      id: mufredat.id,
      mufredatBaslik: mufredat.mufredatBaslik,
      toplamSure: mufredat.toplamSure,
      bolumSayi: mufredat.bolumSayi,
      bolumlers: mufredat.bolumlers,
    });

    this.bolumsSharedCollection = this.bolumService.addBolumToCollectionIfMissing(
      this.bolumsSharedCollection,
      ...(mufredat.bolumlers ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.bolumService
      .query()
      .pipe(map((res: HttpResponse<IBolum[]>) => res.body ?? []))
      .pipe(
        map((bolums: IBolum[]) => this.bolumService.addBolumToCollectionIfMissing(bolums, ...(this.editForm.get('bolumlers')!.value ?? [])))
      )
      .subscribe((bolums: IBolum[]) => (this.bolumsSharedCollection = bolums));
  }

  protected createFromForm(): IMufredat {
    return {
      ...new Mufredat(),
      id: this.editForm.get(['id'])!.value,
      mufredatBaslik: this.editForm.get(['mufredatBaslik'])!.value,
      toplamSure: this.editForm.get(['toplamSure'])!.value,
      bolumSayi: this.editForm.get(['bolumSayi'])!.value,
      bolumlers: this.editForm.get(['bolumlers'])!.value,
    };
  }
}
