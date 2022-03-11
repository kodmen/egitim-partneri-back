import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISiteInfo, SiteInfo } from '../site-info.model';
import { SiteInfoService } from '../service/site-info.service';

@Injectable({ providedIn: 'root' })
export class SiteInfoRoutingResolveService implements Resolve<ISiteInfo> {
  constructor(protected service: SiteInfoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISiteInfo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((siteInfo: HttpResponse<SiteInfo>) => {
          if (siteInfo.body) {
            return of(siteInfo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SiteInfo());
  }
}
