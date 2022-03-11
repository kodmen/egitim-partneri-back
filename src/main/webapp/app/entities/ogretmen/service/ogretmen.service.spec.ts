import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOgretmen, Ogretmen } from '../ogretmen.model';

import { OgretmenService } from './ogretmen.service';

describe('Ogretmen Service', () => {
  let service: OgretmenService;
  let httpMock: HttpTestingController;
  let elemDefault: IOgretmen;
  let expectedResult: IOgretmen | IOgretmen[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OgretmenService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      aciklama: 'AAAAAAA',
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

    it('should create a Ogretmen', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Ogretmen()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Ogretmen', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          aciklama: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Ogretmen', () => {
      const patchObject = Object.assign(
        {
          aciklama: 'BBBBBB',
        },
        new Ogretmen()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Ogretmen', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          aciklama: 'BBBBBB',
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

    it('should delete a Ogretmen', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOgretmenToCollectionIfMissing', () => {
      it('should add a Ogretmen to an empty array', () => {
        const ogretmen: IOgretmen = { id: 123 };
        expectedResult = service.addOgretmenToCollectionIfMissing([], ogretmen);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ogretmen);
      });

      it('should not add a Ogretmen to an array that contains it', () => {
        const ogretmen: IOgretmen = { id: 123 };
        const ogretmenCollection: IOgretmen[] = [
          {
            ...ogretmen,
          },
          { id: 456 },
        ];
        expectedResult = service.addOgretmenToCollectionIfMissing(ogretmenCollection, ogretmen);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Ogretmen to an array that doesn't contain it", () => {
        const ogretmen: IOgretmen = { id: 123 };
        const ogretmenCollection: IOgretmen[] = [{ id: 456 }];
        expectedResult = service.addOgretmenToCollectionIfMissing(ogretmenCollection, ogretmen);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ogretmen);
      });

      it('should add only unique Ogretmen to an array', () => {
        const ogretmenArray: IOgretmen[] = [{ id: 123 }, { id: 456 }, { id: 98094 }];
        const ogretmenCollection: IOgretmen[] = [{ id: 123 }];
        expectedResult = service.addOgretmenToCollectionIfMissing(ogretmenCollection, ...ogretmenArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ogretmen: IOgretmen = { id: 123 };
        const ogretmen2: IOgretmen = { id: 456 };
        expectedResult = service.addOgretmenToCollectionIfMissing([], ogretmen, ogretmen2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ogretmen);
        expect(expectedResult).toContain(ogretmen2);
      });

      it('should accept null and undefined values', () => {
        const ogretmen: IOgretmen = { id: 123 };
        expectedResult = service.addOgretmenToCollectionIfMissing([], null, ogretmen, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ogretmen);
      });

      it('should return initial array if no Ogretmen is added', () => {
        const ogretmenCollection: IOgretmen[] = [{ id: 123 }];
        expectedResult = service.addOgretmenToCollectionIfMissing(ogretmenCollection, undefined, null);
        expect(expectedResult).toEqual(ogretmenCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
