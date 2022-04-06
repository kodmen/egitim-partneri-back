import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITestAnaliz, TestAnaliz } from '../test-analiz.model';
import { TestAnalizService } from '../service/test-analiz.service';

@Injectable({ providedIn: 'root' })
export class TestAnalizRoutingResolveService implements Resolve<ITestAnaliz> {
  constructor(protected service: TestAnalizService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITestAnaliz> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((testAnaliz: HttpResponse<TestAnaliz>) => {
          if (testAnaliz.body) {
            return of(testAnaliz.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TestAnaliz());
  }
}
