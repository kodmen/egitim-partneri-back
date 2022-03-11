import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISiteInfo } from '../site-info.model';

@Component({
  selector: 'jhi-site-info-detail',
  templateUrl: './site-info-detail.component.html',
})
export class SiteInfoDetailComponent implements OnInit {
  siteInfo: ISiteInfo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ siteInfo }) => {
      this.siteInfo = siteInfo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
