import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IKayit, Kayit } from '../kayit.model';
import { KayitService } from '../service/kayit.service';

@Injectable({ providedIn: 'root' })
export class KayitRoutingResolveService implements Resolve<IKayit> {
  constructor(protected service: KayitService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IKayit> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((kayit: HttpResponse<Kayit>) => {
          if (kayit.body) {
            return of(kayit.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Kayit());
  }
}
