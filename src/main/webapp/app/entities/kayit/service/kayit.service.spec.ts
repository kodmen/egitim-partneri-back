import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IKayit, Kayit } from '../kayit.model';

import { KayitService } from './kayit.service';

describe('Kayit Service', () => {
  let service: KayitService;
  let httpMock: HttpTestingController;
  let elemDefault: IKayit;
  let expectedResult: IKayit | IKayit[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(KayitService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      puan: 0,
      kayitTarih: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          kayitTarih: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Kayit', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          kayitTarih: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          kayitTarih: currentDate,
        },
        returnedFromService
      );

      service.create(new Kayit()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Kayit', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          puan: 1,
          kayitTarih: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          kayitTarih: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Kayit', () => {
      const patchObject = Object.assign(
        {
          puan: 1,
        },
        new Kayit()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          kayitTarih: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Kayit', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          puan: 1,
          kayitTarih: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          kayitTarih: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Kayit', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addKayitToCollectionIfMissing', () => {
      it('should add a Kayit to an empty array', () => {
        const kayit: IKayit = { id: 123 };
        expectedResult = service.addKayitToCollectionIfMissing([], kayit);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(kayit);
      });

      it('should not add a Kayit to an array that contains it', () => {
        const kayit: IKayit = { id: 123 };
        const kayitCollection: IKayit[] = [
          {
            ...kayit,
          },
          { id: 456 },
        ];
        expectedResult = service.addKayitToCollectionIfMissing(kayitCollection, kayit);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Kayit to an array that doesn't contain it", () => {
        const kayit: IKayit = { id: 123 };
        const kayitCollection: IKayit[] = [{ id: 456 }];
        expectedResult = service.addKayitToCollectionIfMissing(kayitCollection, kayit);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(kayit);
      });

      it('should add only unique Kayit to an array', () => {
        const kayitArray: IKayit[] = [{ id: 123 }, { id: 456 }, { id: 19406 }];
        const kayitCollection: IKayit[] = [{ id: 123 }];
        expectedResult = service.addKayitToCollectionIfMissing(kayitCollection, ...kayitArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const kayit: IKayit = { id: 123 };
        const kayit2: IKayit = { id: 456 };
        expectedResult = service.addKayitToCollectionIfMissing([], kayit, kayit2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(kayit);
        expect(expectedResult).toContain(kayit2);
      });

      it('should accept null and undefined values', () => {
        const kayit: IKayit = { id: 123 };
        expectedResult = service.addKayitToCollectionIfMissing([], null, kayit, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(kayit);
      });

      it('should return initial array if no Kayit is added', () => {
        const kayitCollection: IKayit[] = [{ id: 123 }];
        expectedResult = service.addKayitToCollectionIfMissing(kayitCollection, undefined, null);
        expect(expectedResult).toEqual(kayitCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
