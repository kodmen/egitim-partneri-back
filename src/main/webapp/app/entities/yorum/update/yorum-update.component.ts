import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IYorum, Yorum } from '../yorum.model';
import { YorumService } from '../service/yorum.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IForm } from 'app/entities/form/form.model';
import { FormService } from 'app/entities/form/service/form.service';

@Component({
  selector: 'jhi-yorum-update',
  templateUrl: './yorum-update.component.html',
})
export class YorumUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  formsSharedCollection: IForm[] = [];

  editForm = this.fb.group({
    id: [],
    yazi: [null, [Validators.maxLength(10000)]],
    date: [],
    userYorum: [],
    formYorum: [],
  });

  constructor(
    protected yorumService: YorumService,
    protected userService: UserService,
    protected formService: FormService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ yorum }) => {
      this.updateForm(yorum);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const yorum = this.createFromForm();
    if (yorum.id !== undefined) {
      this.subscribeToSaveResponse(this.yorumService.update(yorum));
    } else {
      this.subscribeToSaveResponse(this.yorumService.create(yorum));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackFormById(index: number, item: IForm): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IYorum>>): void {
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

  protected updateForm(yorum: IYorum): void {
    this.editForm.patchValue({
      id: yorum.id,
      yazi: yorum.yazi,
      date: yorum.date,
      userYorum: yorum.userYorum,
      formYorum: yorum.formYorum,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, yorum.userYorum);
    this.formsSharedCollection = this.formService.addFormToCollectionIfMissing(this.formsSharedCollection, yorum.formYorum);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('userYorum')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.formService
      .query()
      .pipe(map((res: HttpResponse<IForm[]>) => res.body ?? []))
      .pipe(map((forms: IForm[]) => this.formService.addFormToCollectionIfMissing(forms, this.editForm.get('formYorum')!.value)))
      .subscribe((forms: IForm[]) => (this.formsSharedCollection = forms));
  }

  protected createFromForm(): IYorum {
    return {
      ...new Yorum(),
      id: this.editForm.get(['id'])!.value,
      yazi: this.editForm.get(['yazi'])!.value,
      date: this.editForm.get(['date'])!.value,
      userYorum: this.editForm.get(['userYorum'])!.value,
      formYorum: this.editForm.get(['formYorum'])!.value,
    };
  }
}
