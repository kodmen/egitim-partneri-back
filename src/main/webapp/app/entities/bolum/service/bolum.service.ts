import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBolum, getBolumIdentifier } from '../bolum.model';

export type EntityResponseType = HttpResponse<IBolum>;
export type EntityArrayResponseType = HttpResponse<IBolum[]>;

@Injectable({ providedIn: 'root' })
export class BolumService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bolums');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bolum: IBolum): Observable<EntityResponseType> {
    return this.http.post<IBolum>(this.resourceUrl, bolum, { observe: 'response' });
  }

  update(bolum: IBolum): Observable<EntityResponseType> {
    return this.http.put<IBolum>(`${this.resourceUrl}/${getBolumIdentifier(bolum) as number}`, bolum, { observe: 'response' });
  }

  partialUpdate(bolum: IBolum): Observable<EntityResponseType> {
    return this.http.patch<IBolum>(`${this.resourceUrl}/${getBolumIdentifier(bolum) as number}`, bolum, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBolum>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBolum[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBolumToCollectionIfMissing(bolumCollection: IBolum[], ...bolumsToCheck: (IBolum | null | undefined)[]): IBolum[] {
    const bolums: IBolum[] = bolumsToCheck.filter(isPresent);
    if (bolums.length > 0) {
      const bolumCollectionIdentifiers = bolumCollection.map(bolumItem => getBolumIdentifier(bolumItem)!);
      const bolumsToAdd = bolums.filter(bolumItem => {
        const bolumIdentifier = getBolumIdentifier(bolumItem);
        if (bolumIdentifier == null || bolumCollectionIdentifiers.includes(bolumIdentifier)) {
          return false;
        }
        bolumCollectionIdentifiers.push(bolumIdentifier);
        return true;
      });
      return [...bolumsToAdd, ...bolumCollection];
    }
    return bolumCollection;
  }
}
