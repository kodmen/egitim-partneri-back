import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISiteInfo, getSiteInfoIdentifier } from '../site-info.model';

export type EntityResponseType = HttpResponse<ISiteInfo>;
export type EntityArrayResponseType = HttpResponse<ISiteInfo[]>;

@Injectable({ providedIn: 'root' })
export class SiteInfoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/site-infos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(siteInfo: ISiteInfo): Observable<EntityResponseType> {
    return this.http.post<ISiteInfo>(this.resourceUrl, siteInfo, { observe: 'response' });
  }

  update(siteInfo: ISiteInfo): Observable<EntityResponseType> {
    return this.http.put<ISiteInfo>(`${this.resourceUrl}/${getSiteInfoIdentifier(siteInfo) as number}`, siteInfo, { observe: 'response' });
  }

  partialUpdate(siteInfo: ISiteInfo): Observable<EntityResponseType> {
    return this.http.patch<ISiteInfo>(`${this.resourceUrl}/${getSiteInfoIdentifier(siteInfo) as number}`, siteInfo, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISiteInfo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISiteInfo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSiteInfoToCollectionIfMissing(siteInfoCollection: ISiteInfo[], ...siteInfosToCheck: (ISiteInfo | null | undefined)[]): ISiteInfo[] {
    const siteInfos: ISiteInfo[] = siteInfosToCheck.filter(isPresent);
    if (siteInfos.length > 0) {
      const siteInfoCollectionIdentifiers = siteInfoCollection.map(siteInfoItem => getSiteInfoIdentifier(siteInfoItem)!);
      const siteInfosToAdd = siteInfos.filter(siteInfoItem => {
        const siteInfoIdentifier = getSiteInfoIdentifier(siteInfoItem);
        if (siteInfoIdentifier == null || siteInfoCollectionIdentifiers.includes(siteInfoIdentifier)) {
          return false;
        }
        siteInfoCollectionIdentifiers.push(siteInfoIdentifier);
        return true;
      });
      return [...siteInfosToAdd, ...siteInfoCollection];
    }
    return siteInfoCollection;
  }
}
