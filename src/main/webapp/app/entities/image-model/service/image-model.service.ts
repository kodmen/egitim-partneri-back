import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IImageModel, getImageModelIdentifier } from '../image-model.model';

export type EntityResponseType = HttpResponse<IImageModel>;
export type EntityArrayResponseType = HttpResponse<IImageModel[]>;

@Injectable({ providedIn: 'root' })
export class ImageModelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/image-models');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(imageModel: IImageModel): Observable<EntityResponseType> {
    return this.http.post<IImageModel>(this.resourceUrl, imageModel, { observe: 'response' });
  }

  update(imageModel: IImageModel): Observable<EntityResponseType> {
    return this.http.put<IImageModel>(`${this.resourceUrl}/${getImageModelIdentifier(imageModel) as number}`, imageModel, {
      observe: 'response',
    });
  }

  partialUpdate(imageModel: IImageModel): Observable<EntityResponseType> {
    return this.http.patch<IImageModel>(`${this.resourceUrl}/${getImageModelIdentifier(imageModel) as number}`, imageModel, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IImageModel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IImageModel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addImageModelToCollectionIfMissing(
    imageModelCollection: IImageModel[],
    ...imageModelsToCheck: (IImageModel | null | undefined)[]
  ): IImageModel[] {
    const imageModels: IImageModel[] = imageModelsToCheck.filter(isPresent);
    if (imageModels.length > 0) {
      const imageModelCollectionIdentifiers = imageModelCollection.map(imageModelItem => getImageModelIdentifier(imageModelItem)!);
      const imageModelsToAdd = imageModels.filter(imageModelItem => {
        const imageModelIdentifier = getImageModelIdentifier(imageModelItem);
        if (imageModelIdentifier == null || imageModelCollectionIdentifiers.includes(imageModelIdentifier)) {
          return false;
        }
        imageModelCollectionIdentifiers.push(imageModelIdentifier);
        return true;
      });
      return [...imageModelsToAdd, ...imageModelCollection];
    }
    return imageModelCollection;
  }
}
