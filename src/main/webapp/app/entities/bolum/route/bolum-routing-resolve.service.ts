import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBolum, Bolum } from '../bolum.model';
import { BolumService } from '../service/bolum.service';

@Injectable({ providedIn: 'root' })
export class BolumRoutingResolveService implements Resolve<IBolum> {
  constructor(protected service: BolumService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBolum> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bolum: HttpResponse<Bolum>) => {
          if (bolum.body) {
            return of(bolum.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Bolum());
  }
}
