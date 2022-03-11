import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISoru, Soru } from '../soru.model';
import { SoruService } from '../service/soru.service';
import { ISoruKazanimlari } from 'app/entities/soru-kazanimlari/soru-kazanimlari.model';
import { SoruKazanimlariService } from 'app/entities/soru-kazanimlari/service/soru-kazanimlari.service';

@Component({
  selector: 'jhi-soru-update',
  templateUrl: './soru-update.component.html',
})
export class SoruUpdateComponent implements OnInit {
  isSaving = false;

  soruKazanimlarisSharedCollection: ISoruKazanimlari[] = [];

  editForm = this.fb.group({
    id: [],
    cevap: [],
    kazanimlars: [],
  });

  constructor(
    protected soruService: SoruService,
    protected soruKazanimlariService: SoruKazanimlariService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ soru }) => {
      this.updateForm(soru);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const soru = this.createFromForm();
    if (soru.id !== undefined) {
      this.subscribeToSaveResponse(this.soruService.update(soru));
    } else {
      this.subscribeToSaveResponse(this.soruService.create(soru));
    }
  }

  trackSoruKazanimlariById(index: number, item: ISoruKazanimlari): number {
    return item.id!;
  }

  getSelectedSoruKazanimlari(option: ISoruKazanimlari, selectedVals?: ISoruKazanimlari[]): ISoruKazanimlari {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISoru>>): void {
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

  protected updateForm(soru: ISoru): void {
    this.editForm.patchValue({
      id: soru.id,
      cevap: soru.cevap,
      kazanimlars: soru.kazanimlars,
    });

    this.soruKazanimlarisSharedCollection = this.soruKazanimlariService.addSoruKazanimlariToCollectionIfMissing(
      this.soruKazanimlarisSharedCollection,
      ...(soru.kazanimlars ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.soruKazanimlariService
      .query()
      .pipe(map((res: HttpResponse<ISoruKazanimlari[]>) => res.body ?? []))
      .pipe(
        map((soruKazanimlaris: ISoruKazanimlari[]) =>
          this.soruKazanimlariService.addSoruKazanimlariToCollectionIfMissing(
            soruKazanimlaris,
            ...(this.editForm.get('kazanimlars')!.value ?? [])
          )
        )
      )
      .subscribe((soruKazanimlaris: ISoruKazanimlari[]) => (this.soruKazanimlarisSharedCollection = soruKazanimlaris));
  }

  protected createFromForm(): ISoru {
    return {
      ...new Soru(),
      id: this.editForm.get(['id'])!.value,
      cevap: this.editForm.get(['cevap'])!.value,
      kazanimlars: this.editForm.get(['kazanimlars'])!.value,
    };
  }
}
