import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IYorum, getYorumIdentifier } from '../yorum.model';

export type EntityResponseType = HttpResponse<IYorum>;
export type EntityArrayResponseType = HttpResponse<IYorum[]>;

@Injectable({ providedIn: 'root' })
export class YorumService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/yorums');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(yorum: IYorum): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(yorum);
    return this.http
      .post<IYorum>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(yorum: IYorum): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(yorum);
    return this.http
      .put<IYorum>(`${this.resourceUrl}/${getYorumIdentifier(yorum) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(yorum: IYorum): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(yorum);
    return this.http
      .patch<IYorum>(`${this.resourceUrl}/${getYorumIdentifier(yorum) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IYorum>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IYorum[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addYorumToCollectionIfMissing(yorumCollection: IYorum[], ...yorumsToCheck: (IYorum | null | undefined)[]): IYorum[] {
    const yorums: IYorum[] = yorumsToCheck.filter(isPresent);
    if (yorums.length > 0) {
      const yorumCollectionIdentifiers = yorumCollection.map(yorumItem => getYorumIdentifier(yorumItem)!);
      const yorumsToAdd = yorums.filter(yorumItem => {
        const yorumIdentifier = getYorumIdentifier(yorumItem);
        if (yorumIdentifier == null || yorumCollectionIdentifiers.includes(yorumIdentifier)) {
          return false;
        }
        yorumCollectionIdentifiers.push(yorumIdentifier);
        return true;
      });
      return [...yorumsToAdd, ...yorumCollection];
    }
    return yorumCollection;
  }

  protected convertDateFromClient(yorum: IYorum): IYorum {
    return Object.assign({}, yorum, {
      date: yorum.date?.isValid() ? yorum.date.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((yorum: IYorum) => {
        yorum.date = yorum.date ? dayjs(yorum.date) : undefined;
      });
    }
    return res;
  }
}
