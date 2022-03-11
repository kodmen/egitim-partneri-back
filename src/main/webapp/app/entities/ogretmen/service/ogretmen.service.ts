import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOgretmen, getOgretmenIdentifier } from '../ogretmen.model';

export type EntityResponseType = HttpResponse<IOgretmen>;
export type EntityArrayResponseType = HttpResponse<IOgretmen[]>;

@Injectable({ providedIn: 'root' })
export class OgretmenService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ogretmen');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ogretmen: IOgretmen): Observable<EntityResponseType> {
    return this.http.post<IOgretmen>(this.resourceUrl, ogretmen, { observe: 'response' });
  }

  update(ogretmen: IOgretmen): Observable<EntityResponseType> {
    return this.http.put<IOgretmen>(`${this.resourceUrl}/${getOgretmenIdentifier(ogretmen) as number}`, ogretmen, { observe: 'response' });
  }

  partialUpdate(ogretmen: IOgretmen): Observable<EntityResponseType> {
    return this.http.patch<IOgretmen>(`${this.resourceUrl}/${getOgretmenIdentifier(ogretmen) as number}`, ogretmen, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOgretmen>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOgretmen[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOgretmenToCollectionIfMissing(ogretmenCollection: IOgretmen[], ...ogretmenToCheck: (IOgretmen | null | undefined)[]): IOgretmen[] {
    const ogretmen: IOgretmen[] = ogretmenToCheck.filter(isPresent);
    if (ogretmen.length > 0) {
      const ogretmenCollectionIdentifiers = ogretmenCollection.map(ogretmenItem => getOgretmenIdentifier(ogretmenItem)!);
      const ogretmenToAdd = ogretmen.filter(ogretmenItem => {
        const ogretmenIdentifier = getOgretmenIdentifier(ogretmenItem);
        if (ogretmenIdentifier == null || ogretmenCollectionIdentifiers.includes(ogretmenIdentifier)) {
          return false;
        }
        ogretmenCollectionIdentifiers.push(ogretmenIdentifier);
        return true;
      });
      return [...ogretmenToAdd, ...ogretmenCollection];
    }
    return ogretmenCollection;
  }
}
