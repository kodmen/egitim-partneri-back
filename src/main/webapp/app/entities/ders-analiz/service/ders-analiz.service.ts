import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDersAnaliz, getDersAnalizIdentifier } from '../ders-analiz.model';

export type EntityResponseType = HttpResponse<IDersAnaliz>;
export type EntityArrayResponseType = HttpResponse<IDersAnaliz[]>;

@Injectable({ providedIn: 'root' })
export class DersAnalizService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ders-analizs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dersAnaliz: IDersAnaliz): Observable<EntityResponseType> {
    return this.http.post<IDersAnaliz>(this.resourceUrl, dersAnaliz, { observe: 'response' });
  }

  update(dersAnaliz: IDersAnaliz): Observable<EntityResponseType> {
    return this.http.put<IDersAnaliz>(`${this.resourceUrl}/${getDersAnalizIdentifier(dersAnaliz) as number}`, dersAnaliz, {
      observe: 'response',
    });
  }

  partialUpdate(dersAnaliz: IDersAnaliz): Observable<EntityResponseType> {
    return this.http.patch<IDersAnaliz>(`${this.resourceUrl}/${getDersAnalizIdentifier(dersAnaliz) as number}`, dersAnaliz, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDersAnaliz>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDersAnaliz[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDersAnalizToCollectionIfMissing(
    dersAnalizCollection: IDersAnaliz[],
    ...dersAnalizsToCheck: (IDersAnaliz | null | undefined)[]
  ): IDersAnaliz[] {
    const dersAnalizs: IDersAnaliz[] = dersAnalizsToCheck.filter(isPresent);
    if (dersAnalizs.length > 0) {
      const dersAnalizCollectionIdentifiers = dersAnalizCollection.map(dersAnalizItem => getDersAnalizIdentifier(dersAnalizItem)!);
      const dersAnalizsToAdd = dersAnalizs.filter(dersAnalizItem => {
        const dersAnalizIdentifier = getDersAnalizIdentifier(dersAnalizItem);
        if (dersAnalizIdentifier == null || dersAnalizCollectionIdentifiers.includes(dersAnalizIdentifier)) {
          return false;
        }
        dersAnalizCollectionIdentifiers.push(dersAnalizIdentifier);
        return true;
      });
      return [...dersAnalizsToAdd, ...dersAnalizCollection];
    }
    return dersAnalizCollection;
  }
}
