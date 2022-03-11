import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOgrenci, getOgrenciIdentifier } from '../ogrenci.model';

export type EntityResponseType = HttpResponse<IOgrenci>;
export type EntityArrayResponseType = HttpResponse<IOgrenci[]>;

@Injectable({ providedIn: 'root' })
export class OgrenciService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ogrencis');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ogrenci: IOgrenci): Observable<EntityResponseType> {
    return this.http.post<IOgrenci>(this.resourceUrl, ogrenci, { observe: 'response' });
  }

  update(ogrenci: IOgrenci): Observable<EntityResponseType> {
    return this.http.put<IOgrenci>(`${this.resourceUrl}/${getOgrenciIdentifier(ogrenci) as number}`, ogrenci, { observe: 'response' });
  }

  partialUpdate(ogrenci: IOgrenci): Observable<EntityResponseType> {
    return this.http.patch<IOgrenci>(`${this.resourceUrl}/${getOgrenciIdentifier(ogrenci) as number}`, ogrenci, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOgrenci>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOgrenci[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOgrenciToCollectionIfMissing(ogrenciCollection: IOgrenci[], ...ogrencisToCheck: (IOgrenci | null | undefined)[]): IOgrenci[] {
    const ogrencis: IOgrenci[] = ogrencisToCheck.filter(isPresent);
    if (ogrencis.length > 0) {
      const ogrenciCollectionIdentifiers = ogrenciCollection.map(ogrenciItem => getOgrenciIdentifier(ogrenciItem)!);
      const ogrencisToAdd = ogrencis.filter(ogrenciItem => {
        const ogrenciIdentifier = getOgrenciIdentifier(ogrenciItem);
        if (ogrenciIdentifier == null || ogrenciCollectionIdentifiers.includes(ogrenciIdentifier)) {
          return false;
        }
        ogrenciCollectionIdentifiers.push(ogrenciIdentifier);
        return true;
      });
      return [...ogrencisToAdd, ...ogrenciCollection];
    }
    return ogrenciCollection;
  }
}
