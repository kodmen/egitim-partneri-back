import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDers, getDersIdentifier } from '../ders.model';

export type EntityResponseType = HttpResponse<IDers>;
export type EntityArrayResponseType = HttpResponse<IDers[]>;

@Injectable({ providedIn: 'root' })
export class DersService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ders');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ders: IDers): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ders);
    return this.http
      .post<IDers>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(ders: IDers): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ders);
    return this.http
      .put<IDers>(`${this.resourceUrl}/${getDersIdentifier(ders) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(ders: IDers): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ders);
    return this.http
      .patch<IDers>(`${this.resourceUrl}/${getDersIdentifier(ders) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDers>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDers[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDersToCollectionIfMissing(dersCollection: IDers[], ...dersToCheck: (IDers | null | undefined)[]): IDers[] {
    const ders: IDers[] = dersToCheck.filter(isPresent);
    if (ders.length > 0) {
      const dersCollectionIdentifiers = dersCollection.map(dersItem => getDersIdentifier(dersItem)!);
      const dersToAdd = ders.filter(dersItem => {
        const dersIdentifier = getDersIdentifier(dersItem);
        if (dersIdentifier == null || dersCollectionIdentifiers.includes(dersIdentifier)) {
          return false;
        }
        dersCollectionIdentifiers.push(dersIdentifier);
        return true;
      });
      return [...dersToAdd, ...dersCollection];
    }
    return dersCollection;
  }

  protected convertDateFromClient(ders: IDers): IDers {
    return Object.assign({}, ders, {
      olusturulmaTarih: ders.olusturulmaTarih?.isValid() ? ders.olusturulmaTarih.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.olusturulmaTarih = res.body.olusturulmaTarih ? dayjs(res.body.olusturulmaTarih) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((ders: IDers) => {
        ders.olusturulmaTarih = ders.olusturulmaTarih ? dayjs(ders.olusturulmaTarih) : undefined;
      });
    }
    return res;
  }
}
