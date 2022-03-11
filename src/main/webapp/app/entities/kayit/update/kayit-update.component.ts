import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IKayit, Kayit } from '../kayit.model';
import { KayitService } from '../service/kayit.service';
import { IDersAnaliz } from 'app/entities/ders-analiz/ders-analiz.model';
import { DersAnalizService } from 'app/entities/ders-analiz/service/ders-analiz.service';
import { IDers } from 'app/entities/ders/ders.model';
import { DersService } from 'app/entities/ders/service/ders.service';
import { IOgrenci } from 'app/entities/ogrenci/ogrenci.model';
import { OgrenciService } from 'app/entities/ogrenci/service/ogrenci.service';

@Component({
  selector: 'jhi-kayit-update',
  templateUrl: './kayit-update.component.html',
})
export class KayitUpdateComponent implements OnInit {
  isSaving = false;

  dersAnalizsSharedCollection: IDersAnaliz[] = [];
  dersSharedCollection: IDers[] = [];
  ogrencisSharedCollection: IOgrenci[] = [];

  editForm = this.fb.group({
    id: [],
    puan: [],
    kayitTarih: [],
    dersAnalizleris: [],
    aitOldDers: [],
    kayitOgrenci: [],
  });

  constructor(
    protected kayitService: KayitService,
    protected dersAnalizService: DersAnalizService,
    protected dersService: DersService,
    protected ogrenciService: OgrenciService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ kayit }) => {
      this.updateForm(kayit);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const kayit = this.createFromForm();
    if (kayit.id !== undefined) {
      this.subscribeToSaveResponse(this.kayitService.update(kayit));
    } else {
      this.subscribeToSaveResponse(this.kayitService.create(kayit));
    }
  }

  trackDersAnalizById(index: number, item: IDersAnaliz): number {
    return item.id!;
  }

  trackDersById(index: number, item: IDers): number {
    return item.id!;
  }

  trackOgrenciById(index: number, item: IOgrenci): number {
    return item.id!;
  }

  getSelectedDersAnaliz(option: IDersAnaliz, selectedVals?: IDersAnaliz[]): IDersAnaliz {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKayit>>): void {
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

  protected updateForm(kayit: IKayit): void {
    this.editForm.patchValue({
      id: kayit.id,
      puan: kayit.puan,
      kayitTarih: kayit.kayitTarih,
      dersAnalizleris: kayit.dersAnalizleris,
      aitOldDers: kayit.aitOldDers,
      kayitOgrenci: kayit.kayitOgrenci,
    });

    this.dersAnalizsSharedCollection = this.dersAnalizService.addDersAnalizToCollectionIfMissing(
      this.dersAnalizsSharedCollection,
      ...(kayit.dersAnalizleris ?? [])
    );
    this.dersSharedCollection = this.dersService.addDersToCollectionIfMissing(this.dersSharedCollection, kayit.aitOldDers);
    this.ogrencisSharedCollection = this.ogrenciService.addOgrenciToCollectionIfMissing(this.ogrencisSharedCollection, kayit.kayitOgrenci);
  }

  protected loadRelationshipsOptions(): void {
    this.dersAnalizService
      .query()
      .pipe(map((res: HttpResponse<IDersAnaliz[]>) => res.body ?? []))
      .pipe(
        map((dersAnalizs: IDersAnaliz[]) =>
          this.dersAnalizService.addDersAnalizToCollectionIfMissing(dersAnalizs, ...(this.editForm.get('dersAnalizleris')!.value ?? []))
        )
      )
      .subscribe((dersAnalizs: IDersAnaliz[]) => (this.dersAnalizsSharedCollection = dersAnalizs));

    this.dersService
      .query()
      .pipe(map((res: HttpResponse<IDers[]>) => res.body ?? []))
      .pipe(map((ders: IDers[]) => this.dersService.addDersToCollectionIfMissing(ders, this.editForm.get('aitOldDers')!.value)))
      .subscribe((ders: IDers[]) => (this.dersSharedCollection = ders));

    this.ogrenciService
      .query()
      .pipe(map((res: HttpResponse<IOgrenci[]>) => res.body ?? []))
      .pipe(
        map((ogrencis: IOgrenci[]) =>
          this.ogrenciService.addOgrenciToCollectionIfMissing(ogrencis, this.editForm.get('kayitOgrenci')!.value)
        )
      )
      .subscribe((ogrencis: IOgrenci[]) => (this.ogrencisSharedCollection = ogrencis));
  }

  protected createFromForm(): IKayit {
    return {
      ...new Kayit(),
      id: this.editForm.get(['id'])!.value,
      puan: this.editForm.get(['puan'])!.value,
      kayitTarih: this.editForm.get(['kayitTarih'])!.value,
      dersAnalizleris: this.editForm.get(['dersAnalizleris'])!.value,
      aitOldDers: this.editForm.get(['aitOldDers'])!.value,
      kayitOgrenci: this.editForm.get(['kayitOgrenci'])!.value,
    };
  }
}
