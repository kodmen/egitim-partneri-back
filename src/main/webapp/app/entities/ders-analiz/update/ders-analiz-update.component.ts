import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDersAnaliz, DersAnaliz } from '../ders-analiz.model';
import { DersAnalizService } from '../service/ders-analiz.service';
import { IBolum } from 'app/entities/bolum/bolum.model';
import { BolumService } from 'app/entities/bolum/service/bolum.service';

@Component({
  selector: 'jhi-ders-analiz-update',
  templateUrl: './ders-analiz-update.component.html',
})
export class DersAnalizUpdateComponent implements OnInit {
  isSaving = false;

  bolumsSharedCollection: IBolum[] = [];

  editForm = this.fb.group({
    id: [],
    toplamYanlis: [],
    toplamDogru: [],
    cozulenSoru: [],
    tamamlandi: [],
    aitOldBolum: [],
  });

  constructor(
    protected dersAnalizService: DersAnalizService,
    protected bolumService: BolumService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dersAnaliz }) => {
      this.updateForm(dersAnaliz);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dersAnaliz = this.createFromForm();
    if (dersAnaliz.id !== undefined) {
      this.subscribeToSaveResponse(this.dersAnalizService.update(dersAnaliz));
    } else {
      this.subscribeToSaveResponse(this.dersAnalizService.create(dersAnaliz));
    }
  }

  trackBolumById(index: number, item: IBolum): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDersAnaliz>>): void {
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

  protected updateForm(dersAnaliz: IDersAnaliz): void {
    this.editForm.patchValue({
      id: dersAnaliz.id,
      toplamYanlis: dersAnaliz.toplamYanlis,
      toplamDogru: dersAnaliz.toplamDogru,
      cozulenSoru: dersAnaliz.cozulenSoru,
      tamamlandi: dersAnaliz.tamamlandi,
      aitOldBolum: dersAnaliz.aitOldBolum,
    });

    this.bolumsSharedCollection = this.bolumService.addBolumToCollectionIfMissing(this.bolumsSharedCollection, dersAnaliz.aitOldBolum);
  }

  protected loadRelationshipsOptions(): void {
    this.bolumService
      .query()
      .pipe(map((res: HttpResponse<IBolum[]>) => res.body ?? []))
      .pipe(map((bolums: IBolum[]) => this.bolumService.addBolumToCollectionIfMissing(bolums, this.editForm.get('aitOldBolum')!.value)))
      .subscribe((bolums: IBolum[]) => (this.bolumsSharedCollection = bolums));
  }

  protected createFromForm(): IDersAnaliz {
    return {
      ...new DersAnaliz(),
      id: this.editForm.get(['id'])!.value,
      toplamYanlis: this.editForm.get(['toplamYanlis'])!.value,
      toplamDogru: this.editForm.get(['toplamDogru'])!.value,
      cozulenSoru: this.editForm.get(['cozulenSoru'])!.value,
      tamamlandi: this.editForm.get(['tamamlandi'])!.value,
      aitOldBolum: this.editForm.get(['aitOldBolum'])!.value,
    };
  }
}
