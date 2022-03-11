import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOgretmen, Ogretmen } from '../ogretmen.model';
import { OgretmenService } from '../service/ogretmen.service';

@Injectable({ providedIn: 'root' })
export class OgretmenRoutingResolveService implements Resolve<IOgretmen> {
  constructor(protected service: OgretmenService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOgretmen> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ogretmen: HttpResponse<Ogretmen>) => {
          if (ogretmen.body) {
            return of(ogretmen.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Ogretmen());
  }
}
