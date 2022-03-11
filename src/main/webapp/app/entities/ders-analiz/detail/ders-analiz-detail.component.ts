import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDersAnaliz } from '../ders-analiz.model';

@Component({
  selector: 'jhi-ders-analiz-detail',
  templateUrl: './ders-analiz-detail.component.html',
})
export class DersAnalizDetailComponent implements OnInit {
  dersAnaliz: IDersAnaliz | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dersAnaliz }) => {
      this.dersAnaliz = dersAnaliz;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
