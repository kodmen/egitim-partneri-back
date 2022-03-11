import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISoruTest, SoruTest } from '../soru-test.model';
import { SoruTestService } from '../service/soru-test.service';

@Injectable({ providedIn: 'root' })
export class SoruTestRoutingResolveService implements Resolve<ISoruTest> {
  constructor(protected service: SoruTestService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISoruTest> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((soruTest: HttpResponse<SoruTest>) => {
          if (soruTest.body) {
            return of(soruTest.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SoruTest());
  }
}
