import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISoruKazanimlari, SoruKazanimlari } from '../soru-kazanimlari.model';
import { SoruKazanimlariService } from '../service/soru-kazanimlari.service';

@Injectable({ providedIn: 'root' })
export class SoruKazanimlariRoutingResolveService implements Resolve<ISoruKazanimlari> {
  constructor(protected service: SoruKazanimlariService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISoruKazanimlari> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((soruKazanimlari: HttpResponse<SoruKazanimlari>) => {
          if (soruKazanimlari.body) {
            return of(soruKazanimlari.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SoruKazanimlari());
  }
}
