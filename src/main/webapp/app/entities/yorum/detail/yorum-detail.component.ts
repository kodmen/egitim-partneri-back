import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IYorum } from '../yorum.model';

@Component({
  selector: 'jhi-yorum-detail',
  templateUrl: './yorum-detail.component.html',
})
export class YorumDetailComponent implements OnInit {
  yorum: IYorum | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ yorum }) => {
      this.yorum = yorum;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
