import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITestAnaliz, TestAnaliz } from '../test-analiz.model';
import { TestAnalizService } from '../service/test-analiz.service';
import { IDersAnaliz } from 'app/entities/ders-analiz/ders-analiz.model';
import { DersAnalizService } from 'app/entities/ders-analiz/service/ders-analiz.service';

@Component({
  selector: 'jhi-test-analiz-update',
  templateUrl: './test-analiz-update.component.html',
})
export class TestAnalizUpdateComponent implements OnInit {
  isSaving = false;

  dersAnalizsSharedCollection: IDersAnaliz[] = [];

  editForm = this.fb.group({
    id: [],
    dogru: [],
    yanlis: [],
    bos: [],
    net: [],
    tamamlandi: [],
    testId: [],
    dersAnaliz: [],
  });

  constructor(
    protected testAnalizService: TestAnalizService,
    protected dersAnalizService: DersAnalizService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ testAnaliz }) => {
      this.updateForm(testAnaliz);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const testAnaliz = this.createFromForm();
    if (testAnaliz.id !== undefined) {
      this.subscribeToSaveResponse(this.testAnalizService.update(testAnaliz));
    } else {
      this.subscribeToSaveResponse(this.testAnalizService.create(testAnaliz));
    }
  }

  trackDersAnalizById(index: number, item: IDersAnaliz): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITestAnaliz>>): void {
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

  protected updateForm(testAnaliz: ITestAnaliz): void {
    this.editForm.patchValue({
      id: testAnaliz.id,
      dogru: testAnaliz.dogru,
      yanlis: testAnaliz.yanlis,
      bos: testAnaliz.bos,
      net: testAnaliz.net,
      tamamlandi: testAnaliz.tamamlandi,
      testId: testAnaliz.testId,
      dersAnaliz: testAnaliz.dersAnaliz,
    });

    this.dersAnalizsSharedCollection = this.dersAnalizService.addDersAnalizToCollectionIfMissing(
      this.dersAnalizsSharedCollection,
      testAnaliz.dersAnaliz
    );
  }

  protected loadRelationshipsOptions(): void {
    this.dersAnalizService
      .query()
      .pipe(map((res: HttpResponse<IDersAnaliz[]>) => res.body ?? []))
      .pipe(
        map((dersAnalizs: IDersAnaliz[]) =>
          this.dersAnalizService.addDersAnalizToCollectionIfMissing(dersAnalizs, this.editForm.get('dersAnaliz')!.value)
        )
      )
      .subscribe((dersAnalizs: IDersAnaliz[]) => (this.dersAnalizsSharedCollection = dersAnalizs));
  }

  protected createFromForm(): ITestAnaliz {
    return {
      ...new TestAnaliz(),
      id: this.editForm.get(['id'])!.value,
      dogru: this.editForm.get(['dogru'])!.value,
      yanlis: this.editForm.get(['yanlis'])!.value,
      bos: this.editForm.get(['bos'])!.value,
      net: this.editForm.get(['net'])!.value,
      tamamlandi: this.editForm.get(['tamamlandi'])!.value,
      testId: this.editForm.get(['testId'])!.value,
      dersAnaliz: this.editForm.get(['dersAnaliz'])!.value,
    };
  }
}
