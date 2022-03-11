import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRozet, getRozetIdentifier } from '../rozet.model';

export type EntityResponseType = HttpResponse<IRozet>;
export type EntityArrayResponseType = HttpResponse<IRozet[]>;

@Injectable({ providedIn: 'root' })
export class RozetService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rozets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rozet: IRozet): Observable<EntityResponseType> {
    return this.http.post<IRozet>(this.resourceUrl, rozet, { observe: 'response' });
  }

  update(rozet: IRozet): Observable<EntityResponseType> {
    return this.http.put<IRozet>(`${this.resourceUrl}/${getRozetIdentifier(rozet) as number}`, rozet, { observe: 'response' });
  }

  partialUpdate(rozet: IRozet): Observable<EntityResponseType> {
    return this.http.patch<IRozet>(`${this.resourceUrl}/${getRozetIdentifier(rozet) as number}`, rozet, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRozet>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRozet[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRozetToCollectionIfMissing(rozetCollection: IRozet[], ...rozetsToCheck: (IRozet | null | undefined)[]): IRozet[] {
    const rozets: IRozet[] = rozetsToCheck.filter(isPresent);
    if (rozets.length > 0) {
      const rozetCollectionIdentifiers = rozetCollection.map(rozetItem => getRozetIdentifier(rozetItem)!);
      const rozetsToAdd = rozets.filter(rozetItem => {
        const rozetIdentifier = getRozetIdentifier(rozetItem);
        if (rozetIdentifier == null || rozetCollectionIdentifiers.includes(rozetIdentifier)) {
          return false;
        }
        rozetCollectionIdentifiers.push(rozetIdentifier);
        return true;
      });
      return [...rozetsToAdd, ...rozetCollection];
    }
    return rozetCollection;
  }
}
