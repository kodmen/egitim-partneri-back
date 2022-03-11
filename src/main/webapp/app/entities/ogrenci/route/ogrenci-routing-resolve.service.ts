import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOgrenci, Ogrenci } from '../ogrenci.model';
import { OgrenciService } from '../service/ogrenci.service';

@Injectable({ providedIn: 'root' })
export class OgrenciRoutingResolveService implements Resolve<IOgrenci> {
  constructor(protected service: OgrenciService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOgrenci> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ogrenci: HttpResponse<Ogrenci>) => {
          if (ogrenci.body) {
            return of(ogrenci.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Ogrenci());
  }
}
