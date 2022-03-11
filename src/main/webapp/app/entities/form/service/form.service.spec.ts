import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IForm, Form } from '../form.model';

import { FormService } from './form.service';

describe('Form Service', () => {
  let service: FormService;
  let httpMock: HttpTestingController;
  let elemDefault: IForm;
  let expectedResult: IForm | IForm[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FormService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      baslik: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Form', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Form()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Form', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          baslik: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Form', () => {
      const patchObject = Object.assign(
        {
          baslik: 'BBBBBB',
        },
        new Form()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Form', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          baslik: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Form', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFormToCollectionIfMissing', () => {
      it('should add a Form to an empty array', () => {
        const form: IForm = { id: 123 };
        expectedResult = service.addFormToCollectionIfMissing([], form);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(form);
      });

      it('should not add a Form to an array that contains it', () => {
        const form: IForm = { id: 123 };
        const formCollection: IForm[] = [
          {
            ...form,
          },
          { id: 456 },
        ];
        expectedResult = service.addFormToCollectionIfMissing(formCollection, form);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Form to an array that doesn't contain it", () => {
        const form: IForm = { id: 123 };
        const formCollection: IForm[] = [{ id: 456 }];
        expectedResult = service.addFormToCollectionIfMissing(formCollection, form);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(form);
      });

      it('should add only unique Form to an array', () => {
        const formArray: IForm[] = [{ id: 123 }, { id: 456 }, { id: 11375 }];
        const formCollection: IForm[] = [{ id: 123 }];
        expectedResult = service.addFormToCollectionIfMissing(formCollection, ...formArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const form: IForm = { id: 123 };
        const form2: IForm = { id: 456 };
        expectedResult = service.addFormToCollectionIfMissing([], form, form2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(form);
        expect(expectedResult).toContain(form2);
      });

      it('should accept null and undefined values', () => {
        const form: IForm = { id: 123 };
        expectedResult = service.addFormToCollectionIfMissing([], null, form, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(form);
      });

      it('should return initial array if no Form is added', () => {
        const formCollection: IForm[] = [{ id: 123 }];
        expectedResult = service.addFormToCollectionIfMissing(formCollection, undefined, null);
        expect(expectedResult).toEqual(formCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
