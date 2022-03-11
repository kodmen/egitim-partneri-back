import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMufredat, Mufredat } from '../mufredat.model';
import { MufredatService } from '../service/mufredat.service';

@Injectable({ providedIn: 'root' })
export class MufredatRoutingResolveService implements Resolve<IMufredat> {
  constructor(protected service: MufredatService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMufredat> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((mufredat: HttpResponse<Mufredat>) => {
          if (mufredat.body) {
            return of(mufredat.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Mufredat());
  }
}
