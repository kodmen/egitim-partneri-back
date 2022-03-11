import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOgretmen } from '../ogretmen.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-ogretmen-detail',
  templateUrl: './ogretmen-detail.component.html',
})
export class OgretmenDetailComponent implements OnInit {
  ogretmen: IOgretmen | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ogretmen }) => {
      this.ogretmen = ogretmen;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
