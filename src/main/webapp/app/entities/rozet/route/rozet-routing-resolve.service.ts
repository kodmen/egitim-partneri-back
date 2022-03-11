import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRozet, Rozet } from '../rozet.model';
import { RozetService } from '../service/rozet.service';

@Injectable({ providedIn: 'root' })
export class RozetRoutingResolveService implements Resolve<IRozet> {
  constructor(protected service: RozetService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRozet> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rozet: HttpResponse<Rozet>) => {
          if (rozet.body) {
            return of(rozet.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Rozet());
  }
}
