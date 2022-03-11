import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IOgrenci, Ogrenci } from '../ogrenci.model';
import { OgrenciService } from '../service/ogrenci.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IRozet } from 'app/entities/rozet/rozet.model';
import { RozetService } from 'app/entities/rozet/service/rozet.service';

@Component({
  selector: 'jhi-ogrenci-update',
  templateUrl: './ogrenci-update.component.html',
})
export class OgrenciUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  rozetsSharedCollection: IRozet[] = [];

  editForm = this.fb.group({
    id: [],
    level: [],
    aciklama: [],
    toplamPuan: [],
    studentUser: [],
    rozetlers: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected ogrenciService: OgrenciService,
    protected userService: UserService,
    protected rozetService: RozetService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ogrenci }) => {
      this.updateForm(ogrenci);

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

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ogrenci = this.createFromForm();
    if (ogrenci.id !== undefined) {
      this.subscribeToSaveResponse(this.ogrenciService.update(ogrenci));
    } else {
      this.subscribeToSaveResponse(this.ogrenciService.create(ogrenci));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackRozetById(index: number, item: IRozet): number {
    return item.id!;
  }

  getSelectedRozet(option: IRozet, selectedVals?: IRozet[]): IRozet {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOgrenci>>): void {
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

  protected updateForm(ogrenci: IOgrenci): void {
    this.editForm.patchValue({
      id: ogrenci.id,
      level: ogrenci.level,
      aciklama: ogrenci.aciklama,
      toplamPuan: ogrenci.toplamPuan,
      studentUser: ogrenci.studentUser,
      rozetlers: ogrenci.rozetlers,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, ogrenci.studentUser);
    this.rozetsSharedCollection = this.rozetService.addRozetToCollectionIfMissing(
      this.rozetsSharedCollection,
      ...(ogrenci.rozetlers ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('studentUser')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.rozetService
      .query()
      .pipe(map((res: HttpResponse<IRozet[]>) => res.body ?? []))
      .pipe(
        map((rozets: IRozet[]) => this.rozetService.addRozetToCollectionIfMissing(rozets, ...(this.editForm.get('rozetlers')!.value ?? [])))
      )
      .subscribe((rozets: IRozet[]) => (this.rozetsSharedCollection = rozets));
  }

  protected createFromForm(): IOgrenci {
    return {
      ...new Ogrenci(),
      id: this.editForm.get(['id'])!.value,
      level: this.editForm.get(['level'])!.value,
      aciklama: this.editForm.get(['aciklama'])!.value,
      toplamPuan: this.editForm.get(['toplamPuan'])!.value,
      studentUser: this.editForm.get(['studentUser'])!.value,
      rozetlers: this.editForm.get(['rozetlers'])!.value,
    };
  }
}
