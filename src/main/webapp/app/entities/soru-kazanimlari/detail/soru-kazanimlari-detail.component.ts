import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISoruKazanimlari } from '../soru-kazanimlari.model';

@Component({
  selector: 'jhi-soru-kazanimlari-detail',
  templateUrl: './soru-kazanimlari-detail.component.html',
})
export class SoruKazanimlariDetailComponent implements OnInit {
  soruKazanimlari: ISoruKazanimlari | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ soruKazanimlari }) => {
      this.soruKazanimlari = soruKazanimlari;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
