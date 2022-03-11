import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMufredat } from '../mufredat.model';

@Component({
  selector: 'jhi-mufredat-detail',
  templateUrl: './mufredat-detail.component.html',
})
export class MufredatDetailComponent implements OnInit {
  mufredat: IMufredat | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mufredat }) => {
      this.mufredat = mufredat;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
