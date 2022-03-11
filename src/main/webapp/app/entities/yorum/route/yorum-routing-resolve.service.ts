import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IYorum, Yorum } from '../yorum.model';
import { YorumService } from '../service/yorum.service';

@Injectable({ providedIn: 'root' })
export class YorumRoutingResolveService implements Resolve<IYorum> {
  constructor(protected service: YorumService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IYorum> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((yorum: HttpResponse<Yorum>) => {
          if (yorum.body) {
            return of(yorum.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Yorum());
  }
}
