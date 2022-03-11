import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDers, Ders } from '../ders.model';
import { DersService } from '../service/ders.service';

@Injectable({ providedIn: 'root' })
export class DersRoutingResolveService implements Resolve<IDers> {
  constructor(protected service: DersService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDers> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ders: HttpResponse<Ders>) => {
          if (ders.body) {
            return of(ders.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Ders());
  }
}
