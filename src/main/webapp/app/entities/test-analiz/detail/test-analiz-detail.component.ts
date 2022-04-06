import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITestAnaliz } from '../test-analiz.model';

@Component({
  selector: 'jhi-test-analiz-detail',
  templateUrl: './test-analiz-detail.component.html',
})
export class TestAnalizDetailComponent implements OnInit {
  testAnaliz: ITestAnaliz | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ testAnaliz }) => {
      this.testAnaliz = testAnaliz;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
