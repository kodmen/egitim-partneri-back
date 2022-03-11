import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISoruKazanimlari, SoruKazanimlari } from '../soru-kazanimlari.model';
import { SoruKazanimlariService } from '../service/soru-kazanimlari.service';

@Component({
  selector: 'jhi-soru-kazanimlari-update',
  templateUrl: './soru-kazanimlari-update.component.html',
})
export class SoruKazanimlariUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    kazanim: [],
  });

  constructor(
    protected soruKazanimlariService: SoruKazanimlariService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ soruKazanimlari }) => {
      this.updateForm(soruKazanimlari);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const soruKazanimlari = this.createFromForm();
    if (soruKazanimlari.id !== undefined) {
      this.subscribeToSaveResponse(this.soruKazanimlariService.update(soruKazanimlari));
    } else {
      this.subscribeToSaveResponse(this.soruKazanimlariService.create(soruKazanimlari));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISoruKazanimlari>>): void {
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

  protected updateForm(soruKazanimlari: ISoruKazanimlari): void {
    this.editForm.patchValue({
      id: soruKazanimlari.id,
      kazanim: soruKazanimlari.kazanim,
    });
  }

  protected createFromForm(): ISoruKazanimlari {
    return {
      ...new SoruKazanimlari(),
      id: this.editForm.get(['id'])!.value,
      kazanim: this.editForm.get(['kazanim'])!.value,
    };
  }
}
