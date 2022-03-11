import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISoruKazanimlari, getSoruKazanimlariIdentifier } from '../soru-kazanimlari.model';

export type EntityResponseType = HttpResponse<ISoruKazanimlari>;
export type EntityArrayResponseType = HttpResponse<ISoruKazanimlari[]>;

@Injectable({ providedIn: 'root' })
export class SoruKazanimlariService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/soru-kazanimlaris');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(soruKazanimlari: ISoruKazanimlari): Observable<EntityResponseType> {
    return this.http.post<ISoruKazanimlari>(this.resourceUrl, soruKazanimlari, { observe: 'response' });
  }

  update(soruKazanimlari: ISoruKazanimlari): Observable<EntityResponseType> {
    return this.http.put<ISoruKazanimlari>(
      `${this.resourceUrl}/${getSoruKazanimlariIdentifier(soruKazanimlari) as number}`,
      soruKazanimlari,
      { observe: 'response' }
    );
  }

  partialUpdate(soruKazanimlari: ISoruKazanimlari): Observable<EntityResponseType> {
    return this.http.patch<ISoruKazanimlari>(
      `${this.resourceUrl}/${getSoruKazanimlariIdentifier(soruKazanimlari) as number}`,
      soruKazanimlari,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISoruKazanimlari>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISoruKazanimlari[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSoruKazanimlariToCollectionIfMissing(
    soruKazanimlariCollection: ISoruKazanimlari[],
    ...soruKazanimlarisToCheck: (ISoruKazanimlari | null | undefined)[]
  ): ISoruKazanimlari[] {
    const soruKazanimlaris: ISoruKazanimlari[] = soruKazanimlarisToCheck.filter(isPresent);
    if (soruKazanimlaris.length > 0) {
      const soruKazanimlariCollectionIdentifiers = soruKazanimlariCollection.map(
        soruKazanimlariItem => getSoruKazanimlariIdentifier(soruKazanimlariItem)!
      );
      const soruKazanimlarisToAdd = soruKazanimlaris.filter(soruKazanimlariItem => {
        const soruKazanimlariIdentifier = getSoruKazanimlariIdentifier(soruKazanimlariItem);
        if (soruKazanimlariIdentifier == null || soruKazanimlariCollectionIdentifiers.includes(soruKazanimlariIdentifier)) {
          return false;
        }
        soruKazanimlariCollectionIdentifiers.push(soruKazanimlariIdentifier);
        return true;
      });
      return [...soruKazanimlarisToAdd, ...soruKazanimlariCollection];
    }
    return soruKazanimlariCollection;
  }
}
