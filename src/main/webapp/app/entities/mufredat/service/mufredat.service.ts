import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMufredat, getMufredatIdentifier } from '../mufredat.model';

export type EntityResponseType = HttpResponse<IMufredat>;
export type EntityArrayResponseType = HttpResponse<IMufredat[]>;

@Injectable({ providedIn: 'root' })
export class MufredatService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mufredats');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(mufredat: IMufredat): Observable<EntityResponseType> {
    return this.http.post<IMufredat>(this.resourceUrl, mufredat, { observe: 'response' });
  }

  update(mufredat: IMufredat): Observable<EntityResponseType> {
    return this.http.put<IMufredat>(`${this.resourceUrl}/${getMufredatIdentifier(mufredat) as number}`, mufredat, { observe: 'response' });
  }

  partialUpdate(mufredat: IMufredat): Observable<EntityResponseType> {
    return this.http.patch<IMufredat>(`${this.resourceUrl}/${getMufredatIdentifier(mufredat) as number}`, mufredat, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMufredat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMufredat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMufredatToCollectionIfMissing(mufredatCollection: IMufredat[], ...mufredatsToCheck: (IMufredat | null | undefined)[]): IMufredat[] {
    const mufredats: IMufredat[] = mufredatsToCheck.filter(isPresent);
    if (mufredats.length > 0) {
      const mufredatCollectionIdentifiers = mufredatCollection.map(mufredatItem => getMufredatIdentifier(mufredatItem)!);
      const mufredatsToAdd = mufredats.filter(mufredatItem => {
        const mufredatIdentifier = getMufredatIdentifier(mufredatItem);
        if (mufredatIdentifier == null || mufredatCollectionIdentifiers.includes(mufredatIdentifier)) {
          return false;
        }
        mufredatCollectionIdentifiers.push(mufredatIdentifier);
        return true;
      });
      return [...mufredatsToAdd, ...mufredatCollection];
    }
    return mufredatCollection;
  }
}
