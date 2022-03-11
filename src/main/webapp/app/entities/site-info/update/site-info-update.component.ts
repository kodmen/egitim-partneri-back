import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISiteInfo, SiteInfo } from '../site-info.model';
import { SiteInfoService } from '../service/site-info.service';

@Component({
  selector: 'jhi-site-info-update',
  templateUrl: './site-info-update.component.html',
})
export class SiteInfoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    baslik: [],
  });

  constructor(protected siteInfoService: SiteInfoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ siteInfo }) => {
      this.updateForm(siteInfo);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const siteInfo = this.createFromForm();
    if (siteInfo.id !== undefined) {
      this.subscribeToSaveResponse(this.siteInfoService.update(siteInfo));
    } else {
      this.subscribeToSaveResponse(this.siteInfoService.create(siteInfo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISiteInfo>>): void {
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

  protected updateForm(siteInfo: ISiteInfo): void {
    this.editForm.patchValue({
      id: siteInfo.id,
      baslik: siteInfo.baslik,
    });
  }

  protected createFromForm(): ISiteInfo {
    return {
      ...new SiteInfo(),
      id: this.editForm.get(['id'])!.value,
      baslik: this.editForm.get(['baslik'])!.value,
    };
  }
}
