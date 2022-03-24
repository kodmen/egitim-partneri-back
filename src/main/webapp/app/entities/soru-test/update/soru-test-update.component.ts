import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISoruTest, SoruTest } from '../soru-test.model';
import { SoruTestService } from '../service/soru-test.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ISoru } from 'app/entities/soru/soru.model';
import { SoruService } from 'app/entities/soru/service/soru.service';
import { IBolum } from 'app/entities/bolum/bolum.model';
import { BolumService } from 'app/entities/bolum/service/bolum.service';

@Component({
  selector: 'jhi-soru-test-update',
  templateUrl: './soru-test-update.component.html',
})
export class SoruTestUpdateComponent implements OnInit {
  isSaving = false;

  sorusSharedCollection: ISoru[] = [];
  bolumsSharedCollection: IBolum[] = [];

  editForm = this.fb.group({
    id: [],
    tesBaslik: [null, [Validators.maxLength(500)]],
    testPdf: [null, [Validators.maxLength(500)]],
    testFoto: [],
    testFotoContentType: [],
    cevaplar: [],
    soruPdfFile: [],
    soruPdfFileContentType: [],
    soruSayisi: [],
    sorulars: [],
    testBolum: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected soruTestService: SoruTestService,
    protected soruService: SoruService,
    protected bolumService: BolumService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ soruTest }) => {
      this.updateForm(soruTest);

      this.loadRelationshipsOptions();
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
    const soruTest = this.createFromForm();
    if (soruTest.id !== undefined) {
      this.subscribeToSaveResponse(this.soruTestService.update(soruTest));
    } else {
      this.subscribeToSaveResponse(this.soruTestService.create(soruTest));
    }
  }

  trackSoruById(index: number, item: ISoru): number {
    return item.id!;
  }

  trackBolumById(index: number, item: IBolum): number {
    return item.id!;
  }

  getSelectedSoru(option: ISoru, selectedVals?: ISoru[]): ISoru {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISoruTest>>): void {
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

  protected updateForm(soruTest: ISoruTest): void {
    this.editForm.patchValue({
      id: soruTest.id,
      tesBaslik: soruTest.tesBaslik,
      testPdf: soruTest.testPdf,
      testFoto: soruTest.testFoto,
      testFotoContentType: soruTest.testFotoContentType,
      cevaplar: soruTest.cevaplar,
      soruPdfFile: soruTest.soruPdfFile,
      soruPdfFileContentType: soruTest.soruPdfFileContentType,
      soruSayisi: soruTest.soruSayisi,
      sorulars: soruTest.sorulars,
      testBolum: soruTest.testBolum,
    });

    this.sorusSharedCollection = this.soruService.addSoruToCollectionIfMissing(this.sorusSharedCollection, ...(soruTest.sorulars ?? []));
    this.bolumsSharedCollection = this.bolumService.addBolumToCollectionIfMissing(this.bolumsSharedCollection, soruTest.testBolum);
  }

  protected loadRelationshipsOptions(): void {
    this.soruService
      .query()
      .pipe(map((res: HttpResponse<ISoru[]>) => res.body ?? []))
      .pipe(map((sorus: ISoru[]) => this.soruService.addSoruToCollectionIfMissing(sorus, ...(this.editForm.get('sorulars')!.value ?? []))))
      .subscribe((sorus: ISoru[]) => (this.sorusSharedCollection = sorus));

    this.bolumService
      .query()
      .pipe(map((res: HttpResponse<IBolum[]>) => res.body ?? []))
      .pipe(map((bolums: IBolum[]) => this.bolumService.addBolumToCollectionIfMissing(bolums, this.editForm.get('testBolum')!.value)))
      .subscribe((bolums: IBolum[]) => (this.bolumsSharedCollection = bolums));
  }

  protected createFromForm(): ISoruTest {
    return {
      ...new SoruTest(),
      id: this.editForm.get(['id'])!.value,
      tesBaslik: this.editForm.get(['tesBaslik'])!.value,
      testPdf: this.editForm.get(['testPdf'])!.value,
      testFotoContentType: this.editForm.get(['testFotoContentType'])!.value,
      testFoto: this.editForm.get(['testFoto'])!.value,
      cevaplar: this.editForm.get(['cevaplar'])!.value,
      soruPdfFileContentType: this.editForm.get(['soruPdfFileContentType'])!.value,
      soruPdfFile: this.editForm.get(['soruPdfFile'])!.value,
      soruSayisi: this.editForm.get(['soruSayisi'])!.value,
      sorulars: this.editForm.get(['sorulars'])!.value,
      testBolum: this.editForm.get(['testBolum'])!.value,
    };
  }
}
