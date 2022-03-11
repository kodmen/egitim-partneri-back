import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDersAnaliz, DersAnaliz } from '../ders-analiz.model';
import { DersAnalizService } from '../service/ders-analiz.service';

@Injectable({ providedIn: 'root' })
export class DersAnalizRoutingResolveService implements Resolve<IDersAnaliz> {
  constructor(protected service: DersAnalizService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDersAnaliz> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dersAnaliz: HttpResponse<DersAnaliz>) => {
          if (dersAnaliz.body) {
            return of(dersAnaliz.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DersAnaliz());
  }
}
