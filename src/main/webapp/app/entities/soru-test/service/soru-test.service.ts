import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISoruTest, getSoruTestIdentifier } from '../soru-test.model';

export type EntityResponseType = HttpResponse<ISoruTest>;
export type EntityArrayResponseType = HttpResponse<ISoruTest[]>;

@Injectable({ providedIn: 'root' })
export class SoruTestService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/soru-tests');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(soruTest: ISoruTest): Observable<EntityResponseType> {
    return this.http.post<ISoruTest>(this.resourceUrl, soruTest, { observe: 'response' });
  }

  update(soruTest: ISoruTest): Observable<EntityResponseType> {
    return this.http.put<ISoruTest>(`${this.resourceUrl}/${getSoruTestIdentifier(soruTest) as number}`, soruTest, { observe: 'response' });
  }

  partialUpdate(soruTest: ISoruTest): Observable<EntityResponseType> {
    return this.http.patch<ISoruTest>(`${this.resourceUrl}/${getSoruTestIdentifier(soruTest) as number}`, soruTest, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISoruTest>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISoruTest[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSoruTestToCollectionIfMissing(soruTestCollection: ISoruTest[], ...soruTestsToCheck: (ISoruTest | null | undefined)[]): ISoruTest[] {
    const soruTests: ISoruTest[] = soruTestsToCheck.filter(isPresent);
    if (soruTests.length > 0) {
      const soruTestCollectionIdentifiers = soruTestCollection.map(soruTestItem => getSoruTestIdentifier(soruTestItem)!);
      const soruTestsToAdd = soruTests.filter(soruTestItem => {
        const soruTestIdentifier = getSoruTestIdentifier(soruTestItem);
        if (soruTestIdentifier == null || soruTestCollectionIdentifiers.includes(soruTestIdentifier)) {
          return false;
        }
        soruTestCollectionIdentifiers.push(soruTestIdentifier);
        return true;
      });
      return [...soruTestsToAdd, ...soruTestCollection];
    }
    return soruTestCollection;
  }
}
