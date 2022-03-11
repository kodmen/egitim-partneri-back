import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IForm, Form } from '../form.model';
import { FormService } from '../service/form.service';

@Injectable({ providedIn: 'root' })
export class FormRoutingResolveService implements Resolve<IForm> {
  constructor(protected service: FormService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IForm> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((form: HttpResponse<Form>) => {
          if (form.body) {
            return of(form.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Form());
  }
}
