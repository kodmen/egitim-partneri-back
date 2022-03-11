import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDers, Ders } from '../ders.model';
import { DersService } from '../service/ders.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IMufredat } from 'app/entities/mufredat/mufredat.model';
import { MufredatService } from 'app/entities/mufredat/service/mufredat.service';
import { IForm } from 'app/entities/form/form.model';
import { FormService } from 'app/entities/form/service/form.service';
import { IOgretmen } from 'app/entities/ogretmen/ogretmen.model';
import { OgretmenService } from 'app/entities/ogretmen/service/ogretmen.service';

@Component({
  selector: 'jhi-ders-update',
  templateUrl: './ders-update.component.html',
})
export class DersUpdateComponent implements OnInit {
  isSaving = false;

  dersMufredatsCollection: IMufredat[] = [];
  dersFormsCollection: IForm[] = [];
  ogretmenSharedCollection: IOgretmen[] = [];

  editForm = this.fb.group({
    id: [],
    isim: [null, [Validators.maxLength(500)]],
    toplamPuan: [],
    olusturulmaTarih: [],
    aciklama: [],
    resim: [],
    resimContentType: [],
    dersMufredat: [],
    dersForm: [],
    dersOgretmeni: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected dersService: DersService,
    protected mufredatService: MufredatService,
    protected formService: FormService,
    protected ogretmenService: OgretmenService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ders }) => {
      this.updateForm(ders);

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
    const ders = this.createFromForm();
    if (ders.id !== undefined) {
      this.subscribeToSaveResponse(this.dersService.update(ders));
    } else {
      this.subscribeToSaveResponse(this.dersService.create(ders));
    }
  }

  trackMufredatById(index: number, item: IMufredat): number {
    return item.id!;
  }

  trackFormById(index: number, item: IForm): number {
    return item.id!;
  }

  trackOgretmenById(index: number, item: IOgretmen): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDers>>): void {
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

  protected updateForm(ders: IDers): void {
    this.editForm.patchValue({
      id: ders.id,
      isim: ders.isim,
      toplamPuan: ders.toplamPuan,
      olusturulmaTarih: ders.olusturulmaTarih,
      aciklama: ders.aciklama,
      resim: ders.resim,
      resimContentType: ders.resimContentType,
      dersMufredat: ders.dersMufredat,
      dersForm: ders.dersForm,
      dersOgretmeni: ders.dersOgretmeni,
    });

    this.dersMufredatsCollection = this.mufredatService.addMufredatToCollectionIfMissing(this.dersMufredatsCollection, ders.dersMufredat);
    this.dersFormsCollection = this.formService.addFormToCollectionIfMissing(this.dersFormsCollection, ders.dersForm);
    this.ogretmenSharedCollection = this.ogretmenService.addOgretmenToCollectionIfMissing(
      this.ogretmenSharedCollection,
      ders.dersOgretmeni
    );
  }

  protected loadRelationshipsOptions(): void {
    this.mufredatService
      .query({ filter: 'mufredatders-is-null' })
      .pipe(map((res: HttpResponse<IMufredat[]>) => res.body ?? []))
      .pipe(
        map((mufredats: IMufredat[]) =>
          this.mufredatService.addMufredatToCollectionIfMissing(mufredats, this.editForm.get('dersMufredat')!.value)
        )
      )
      .subscribe((mufredats: IMufredat[]) => (this.dersMufredatsCollection = mufredats));

    this.formService
      .query({ filter: 'formders-is-null' })
      .pipe(map((res: HttpResponse<IForm[]>) => res.body ?? []))
      .pipe(map((forms: IForm[]) => this.formService.addFormToCollectionIfMissing(forms, this.editForm.get('dersForm')!.value)))
      .subscribe((forms: IForm[]) => (this.dersFormsCollection = forms));

    this.ogretmenService
      .query()
      .pipe(map((res: HttpResponse<IOgretmen[]>) => res.body ?? []))
      .pipe(
        map((ogretmen: IOgretmen[]) =>
          this.ogretmenService.addOgretmenToCollectionIfMissing(ogretmen, this.editForm.get('dersOgretmeni')!.value)
        )
      )
      .subscribe((ogretmen: IOgretmen[]) => (this.ogretmenSharedCollection = ogretmen));
  }

  protected createFromForm(): IDers {
    return {
      ...new Ders(),
      id: this.editForm.get(['id'])!.value,
      isim: this.editForm.get(['isim'])!.value,
      toplamPuan: this.editForm.get(['toplamPuan'])!.value,
      olusturulmaTarih: this.editForm.get(['olusturulmaTarih'])!.value,
      aciklama: this.editForm.get(['aciklama'])!.value,
      resimContentType: this.editForm.get(['resimContentType'])!.value,
      resim: this.editForm.get(['resim'])!.value,
      dersMufredat: this.editForm.get(['dersMufredat'])!.value,
      dersForm: this.editForm.get(['dersForm'])!.value,
      dersOgretmeni: this.editForm.get(['dersOgretmeni'])!.value,
    };
  }
}
