import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IDers, Ders } from '../ders.model';

import { DersService } from './ders.service';

describe('Ders Service', () => {
  let service: DersService;
  let httpMock: HttpTestingController;
  let elemDefault: IDers;
  let expectedResult: IDers | IDers[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DersService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      isim: 'AAAAAAA',
      toplamPuan: 0,
      olusturulmaTarih: currentDate,
      aciklama: 'AAAAAAA',
      resimContentType: 'image/png',
      resim: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          olusturulmaTarih: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Ders', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          olusturulmaTarih: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          olusturulmaTarih: currentDate,
        },
        returnedFromService
      );

      service.create(new Ders()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Ders', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          isim: 'BBBBBB',
          toplamPuan: 1,
          olusturulmaTarih: currentDate.format(DATE_FORMAT),
          aciklama: 'BBBBBB',
          resim: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          olusturulmaTarih: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Ders', () => {
      const patchObject = Object.assign(
        {
          resim: 'BBBBBB',
        },
        new Ders()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          olusturulmaTarih: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Ders', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          isim: 'BBBBBB',
          toplamPuan: 1,
          olusturulmaTarih: currentDate.format(DATE_FORMAT),
          aciklama: 'BBBBBB',
          resim: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          olusturulmaTarih: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Ders', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDersToCollectionIfMissing', () => {
      it('should add a Ders to an empty array', () => {
        const ders: IDers = { id: 123 };
        expectedResult = service.addDersToCollectionIfMissing([], ders);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ders);
      });

      it('should not add a Ders to an array that contains it', () => {
        const ders: IDers = { id: 123 };
        const dersCollection: IDers[] = [
          {
            ...ders,
          },
          { id: 456 },
        ];
        expectedResult = service.addDersToCollectionIfMissing(dersCollection, ders);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Ders to an array that doesn't contain it", () => {
        const ders: IDers = { id: 123 };
        const dersCollection: IDers[] = [{ id: 456 }];
        expectedResult = service.addDersToCollectionIfMissing(dersCollection, ders);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ders);
      });

      it('should add only unique Ders to an array', () => {
        const dersArray: IDers[] = [{ id: 123 }, { id: 456 }, { id: 40987 }];
        const dersCollection: IDers[] = [{ id: 123 }];
        expectedResult = service.addDersToCollectionIfMissing(dersCollection, ...dersArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ders: IDers = { id: 123 };
        const ders2: IDers = { id: 456 };
        expectedResult = service.addDersToCollectionIfMissing([], ders, ders2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ders);
        expect(expectedResult).toContain(ders2);
      });

      it('should accept null and undefined values', () => {
        const ders: IDers = { id: 123 };
        expectedResult = service.addDersToCollectionIfMissing([], null, ders, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ders);
      });

      it('should return initial array if no Ders is added', () => {
        const dersCollection: IDers[] = [{ id: 123 }];
        expectedResult = service.addDersToCollectionIfMissing(dersCollection, undefined, null);
        expect(expectedResult).toEqual(dersCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
