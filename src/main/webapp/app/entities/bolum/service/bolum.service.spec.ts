import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBolum, Bolum } from '../bolum.model';

import { BolumService } from './bolum.service';

describe('Bolum Service', () => {
  let service: BolumService;
  let httpMock: HttpTestingController;
  let elemDefault: IBolum;
  let expectedResult: IBolum | IBolum[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BolumService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      bolumBaslik: 'AAAAAAA',
      dokuman: 'AAAAAAA',
      puan: 0,
      videoLink: 'AAAAAAA',
      sure: 'AAAAAAA',
      sira: 0,
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

    it('should create a Bolum', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Bolum()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Bolum', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          bolumBaslik: 'BBBBBB',
          dokuman: 'BBBBBB',
          puan: 1,
          videoLink: 'BBBBBB',
          sure: 'BBBBBB',
          sira: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Bolum', () => {
      const patchObject = Object.assign(
        {
          videoLink: 'BBBBBB',
          sure: 'BBBBBB',
          sira: 1,
        },
        new Bolum()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Bolum', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          bolumBaslik: 'BBBBBB',
          dokuman: 'BBBBBB',
          puan: 1,
          videoLink: 'BBBBBB',
          sure: 'BBBBBB',
          sira: 1,
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

    it('should delete a Bolum', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBolumToCollectionIfMissing', () => {
      it('should add a Bolum to an empty array', () => {
        const bolum: IBolum = { id: 123 };
        expectedResult = service.addBolumToCollectionIfMissing([], bolum);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bolum);
      });

      it('should not add a Bolum to an array that contains it', () => {
        const bolum: IBolum = { id: 123 };
        const bolumCollection: IBolum[] = [
          {
            ...bolum,
          },
          { id: 456 },
        ];
        expectedResult = service.addBolumToCollectionIfMissing(bolumCollection, bolum);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Bolum to an array that doesn't contain it", () => {
        const bolum: IBolum = { id: 123 };
        const bolumCollection: IBolum[] = [{ id: 456 }];
        expectedResult = service.addBolumToCollectionIfMissing(bolumCollection, bolum);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bolum);
      });

      it('should add only unique Bolum to an array', () => {
        const bolumArray: IBolum[] = [{ id: 123 }, { id: 456 }, { id: 17106 }];
        const bolumCollection: IBolum[] = [{ id: 123 }];
        expectedResult = service.addBolumToCollectionIfMissing(bolumCollection, ...bolumArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bolum: IBolum = { id: 123 };
        const bolum2: IBolum = { id: 456 };
        expectedResult = service.addBolumToCollectionIfMissing([], bolum, bolum2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bolum);
        expect(expectedResult).toContain(bolum2);
      });

      it('should accept null and undefined values', () => {
        const bolum: IBolum = { id: 123 };
        expectedResult = service.addBolumToCollectionIfMissing([], null, bolum, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bolum);
      });

      it('should return initial array if no Bolum is added', () => {
        const bolumCollection: IBolum[] = [{ id: 123 }];
        expectedResult = service.addBolumToCollectionIfMissing(bolumCollection, undefined, null);
        expect(expectedResult).toEqual(bolumCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
