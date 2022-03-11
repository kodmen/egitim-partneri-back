import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDersAnaliz, DersAnaliz } from '../ders-analiz.model';

import { DersAnalizService } from './ders-analiz.service';

describe('DersAnaliz Service', () => {
  let service: DersAnalizService;
  let httpMock: HttpTestingController;
  let elemDefault: IDersAnaliz;
  let expectedResult: IDersAnaliz | IDersAnaliz[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DersAnalizService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      toplamYanlis: 0,
      toplamDogru: 0,
      cozulenSoru: 0,
      tamamlandi: false,
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

    it('should create a DersAnaliz', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new DersAnaliz()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DersAnaliz', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          toplamYanlis: 1,
          toplamDogru: 1,
          cozulenSoru: 1,
          tamamlandi: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DersAnaliz', () => {
      const patchObject = Object.assign(
        {
          toplamYanlis: 1,
          toplamDogru: 1,
        },
        new DersAnaliz()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DersAnaliz', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          toplamYanlis: 1,
          toplamDogru: 1,
          cozulenSoru: 1,
          tamamlandi: true,
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

    it('should delete a DersAnaliz', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDersAnalizToCollectionIfMissing', () => {
      it('should add a DersAnaliz to an empty array', () => {
        const dersAnaliz: IDersAnaliz = { id: 123 };
        expectedResult = service.addDersAnalizToCollectionIfMissing([], dersAnaliz);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dersAnaliz);
      });

      it('should not add a DersAnaliz to an array that contains it', () => {
        const dersAnaliz: IDersAnaliz = { id: 123 };
        const dersAnalizCollection: IDersAnaliz[] = [
          {
            ...dersAnaliz,
          },
          { id: 456 },
        ];
        expectedResult = service.addDersAnalizToCollectionIfMissing(dersAnalizCollection, dersAnaliz);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DersAnaliz to an array that doesn't contain it", () => {
        const dersAnaliz: IDersAnaliz = { id: 123 };
        const dersAnalizCollection: IDersAnaliz[] = [{ id: 456 }];
        expectedResult = service.addDersAnalizToCollectionIfMissing(dersAnalizCollection, dersAnaliz);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dersAnaliz);
      });

      it('should add only unique DersAnaliz to an array', () => {
        const dersAnalizArray: IDersAnaliz[] = [{ id: 123 }, { id: 456 }, { id: 54545 }];
        const dersAnalizCollection: IDersAnaliz[] = [{ id: 123 }];
        expectedResult = service.addDersAnalizToCollectionIfMissing(dersAnalizCollection, ...dersAnalizArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dersAnaliz: IDersAnaliz = { id: 123 };
        const dersAnaliz2: IDersAnaliz = { id: 456 };
        expectedResult = service.addDersAnalizToCollectionIfMissing([], dersAnaliz, dersAnaliz2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dersAnaliz);
        expect(expectedResult).toContain(dersAnaliz2);
      });

      it('should accept null and undefined values', () => {
        const dersAnaliz: IDersAnaliz = { id: 123 };
        expectedResult = service.addDersAnalizToCollectionIfMissing([], null, dersAnaliz, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dersAnaliz);
      });

      it('should return initial array if no DersAnaliz is added', () => {
        const dersAnalizCollection: IDersAnaliz[] = [{ id: 123 }];
        expectedResult = service.addDersAnalizToCollectionIfMissing(dersAnalizCollection, undefined, null);
        expect(expectedResult).toEqual(dersAnalizCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
