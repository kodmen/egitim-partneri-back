import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IForm, getFormIdentifier } from '../form.model';

export type EntityResponseType = HttpResponse<IForm>;
export type EntityArrayResponseType = HttpResponse<IForm[]>;

@Injectable({ providedIn: 'root' })
export class FormService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/forms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(form: IForm): Observable<EntityResponseType> {
    return this.http.post<IForm>(this.resourceUrl, form, { observe: 'response' });
  }

  update(form: IForm): Observable<EntityResponseType> {
    return this.http.put<IForm>(`${this.resourceUrl}/${getFormIdentifier(form) as number}`, form, { observe: 'response' });
  }

  partialUpdate(form: IForm): Observable<EntityResponseType> {
    return this.http.patch<IForm>(`${this.resourceUrl}/${getFormIdentifier(form) as number}`, form, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IForm>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IForm[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFormToCollectionIfMissing(formCollection: IForm[], ...formsToCheck: (IForm | null | undefined)[]): IForm[] {
    const forms: IForm[] = formsToCheck.filter(isPresent);
    if (forms.length > 0) {
      const formCollectionIdentifiers = formCollection.map(formItem => getFormIdentifier(formItem)!);
      const formsToAdd = forms.filter(formItem => {
        const formIdentifier = getFormIdentifier(formItem);
        if (formIdentifier == null || formCollectionIdentifiers.includes(formIdentifier)) {
          return false;
        }
        formCollectionIdentifiers.push(formIdentifier);
        return true;
      });
      return [...formsToAdd, ...formCollection];
    }
    return formCollection;
  }
}
