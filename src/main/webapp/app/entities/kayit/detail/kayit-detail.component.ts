import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IKayit } from '../kayit.model';

@Component({
  selector: 'jhi-kayit-detail',
  templateUrl: './kayit-detail.component.html',
})
export class KayitDetailComponent implements OnInit {
  kayit: IKayit | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ kayit }) => {
      this.kayit = kayit;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
