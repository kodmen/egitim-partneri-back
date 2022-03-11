import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IKayit, getKayitIdentifier } from '../kayit.model';

export type EntityResponseType = HttpResponse<IKayit>;
export type EntityArrayResponseType = HttpResponse<IKayit[]>;

@Injectable({ providedIn: 'root' })
export class KayitService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/kayits');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(kayit: IKayit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(kayit);
    return this.http
      .post<IKayit>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(kayit: IKayit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(kayit);
    return this.http
      .put<IKayit>(`${this.resourceUrl}/${getKayitIdentifier(kayit) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(kayit: IKayit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(kayit);
    return this.http
      .patch<IKayit>(`${this.resourceUrl}/${getKayitIdentifier(kayit) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IKayit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IKayit[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addKayitToCollectionIfMissing(kayitCollection: IKayit[], ...kayitsToCheck: (IKayit | null | undefined)[]): IKayit[] {
    const kayits: IKayit[] = kayitsToCheck.filter(isPresent);
    if (kayits.length > 0) {
      const kayitCollectionIdentifiers = kayitCollection.map(kayitItem => getKayitIdentifier(kayitItem)!);
      const kayitsToAdd = kayits.filter(kayitItem => {
        const kayitIdentifier = getKayitIdentifier(kayitItem);
        if (kayitIdentifier == null || kayitCollectionIdentifiers.includes(kayitIdentifier)) {
          return false;
        }
        kayitCollectionIdentifiers.push(kayitIdentifier);
        return true;
      });
      return [...kayitsToAdd, ...kayitCollection];
    }
    return kayitCollection;
  }

  protected convertDateFromClient(kayit: IKayit): IKayit {
    return Object.assign({}, kayit, {
      kayitTarih: kayit.kayitTarih?.isValid() ? kayit.kayitTarih.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.kayitTarih = res.body.kayitTarih ? dayjs(res.body.kayitTarih) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((kayit: IKayit) => {
        kayit.kayitTarih = kayit.kayitTarih ? dayjs(kayit.kayitTarih) : undefined;
      });
    }
    return res;
  }
}
