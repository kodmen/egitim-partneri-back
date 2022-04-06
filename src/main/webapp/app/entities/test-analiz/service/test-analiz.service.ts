import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITestAnaliz, getTestAnalizIdentifier } from '../test-analiz.model';

export type EntityResponseType = HttpResponse<ITestAnaliz>;
export type EntityArrayResponseType = HttpResponse<ITestAnaliz[]>;

@Injectable({ providedIn: 'root' })
export class TestAnalizService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/test-analizs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(testAnaliz: ITestAnaliz): Observable<EntityResponseType> {
    return this.http.post<ITestAnaliz>(this.resourceUrl, testAnaliz, { observe: 'response' });
  }

  update(testAnaliz: ITestAnaliz): Observable<EntityResponseType> {
    return this.http.put<ITestAnaliz>(`${this.resourceUrl}/${getTestAnalizIdentifier(testAnaliz) as number}`, testAnaliz, {
      observe: 'response',
    });
  }

  partialUpdate(testAnaliz: ITestAnaliz): Observable<EntityResponseType> {
    return this.http.patch<ITestAnaliz>(`${this.resourceUrl}/${getTestAnalizIdentifier(testAnaliz) as number}`, testAnaliz, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITestAnaliz>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITestAnaliz[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTestAnalizToCollectionIfMissing(
    testAnalizCollection: ITestAnaliz[],
    ...testAnalizsToCheck: (ITestAnaliz | null | undefined)[]
  ): ITestAnaliz[] {
    const testAnalizs: ITestAnaliz[] = testAnalizsToCheck.filter(isPresent);
    if (testAnalizs.length > 0) {
      const testAnalizCollectionIdentifiers = testAnalizCollection.map(testAnalizItem => getTestAnalizIdentifier(testAnalizItem)!);
      const testAnalizsToAdd = testAnalizs.filter(testAnalizItem => {
        const testAnalizIdentifier = getTestAnalizIdentifier(testAnalizItem);
        if (testAnalizIdentifier == null || testAnalizCollectionIdentifiers.includes(testAnalizIdentifier)) {
          return false;
        }
        testAnalizCollectionIdentifiers.push(testAnalizIdentifier);
        return true;
      });
      return [...testAnalizsToAdd, ...testAnalizCollection];
    }
    return testAnalizCollection;
  }
}
