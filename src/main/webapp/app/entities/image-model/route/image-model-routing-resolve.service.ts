import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IImageModel, ImageModel } from '../image-model.model';
import { ImageModelService } from '../service/image-model.service';

@Injectable({ providedIn: 'root' })
export class ImageModelRoutingResolveService implements Resolve<IImageModel> {
  constructor(protected service: ImageModelService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IImageModel> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((imageModel: HttpResponse<ImageModel>) => {
          if (imageModel.body) {
            return of(imageModel.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ImageModel());
  }
}
