import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOgrenci, Ogrenci } from '../ogrenci.model';

import { OgrenciService } from './ogrenci.service';

describe('Ogrenci Service', () => {
  let service: OgrenciService;
  let httpMock: HttpTestingController;
  let elemDefault: IOgrenci;
  let expectedResult: IOgrenci | IOgrenci[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OgrenciService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      level: 0,
      aciklama: 'AAAAAAA',
      toplamPuan: 0,
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

    it('should create a Ogrenci', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Ogrenci()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Ogrenci', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          level: 1,
          aciklama: 'BBBBBB',
          toplamPuan: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Ogrenci', () => {
      const patchObject = Object.assign(
        {
          level: 1,
          aciklama: 'BBBBBB',
          toplamPuan: 1,
        },
        new Ogrenci()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Ogrenci', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          level: 1,
          aciklama: 'BBBBBB',
          toplamPuan: 1,
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

    it('should delete a Ogrenci', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOgrenciToCollectionIfMissing', () => {
      it('should add a Ogrenci to an empty array', () => {
        const ogrenci: IOgrenci = { id: 123 };
        expectedResult = service.addOgrenciToCollectionIfMissing([], ogrenci);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ogrenci);
      });

      it('should not add a Ogrenci to an array that contains it', () => {
        const ogrenci: IOgrenci = { id: 123 };
        const ogrenciCollection: IOgrenci[] = [
          {
            ...ogrenci,
          },
          { id: 456 },
        ];
        expectedResult = service.addOgrenciToCollectionIfMissing(ogrenciCollection, ogrenci);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Ogrenci to an array that doesn't contain it", () => {
        const ogrenci: IOgrenci = { id: 123 };
        const ogrenciCollection: IOgrenci[] = [{ id: 456 }];
        expectedResult = service.addOgrenciToCollectionIfMissing(ogrenciCollection, ogrenci);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ogrenci);
      });

      it('should add only unique Ogrenci to an array', () => {
        const ogrenciArray: IOgrenci[] = [{ id: 123 }, { id: 456 }, { id: 75349 }];
        const ogrenciCollection: IOgrenci[] = [{ id: 123 }];
        expectedResult = service.addOgrenciToCollectionIfMissing(ogrenciCollection, ...ogrenciArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ogrenci: IOgrenci = { id: 123 };
        const ogrenci2: IOgrenci = { id: 456 };
        expectedResult = service.addOgrenciToCollectionIfMissing([], ogrenci, ogrenci2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ogrenci);
        expect(expectedResult).toContain(ogrenci2);
      });

      it('should accept null and undefined values', () => {
        const ogrenci: IOgrenci = { id: 123 };
        expectedResult = service.addOgrenciToCollectionIfMissing([], null, ogrenci, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ogrenci);
      });

      it('should return initial array if no Ogrenci is added', () => {
        const ogrenciCollection: IOgrenci[] = [{ id: 123 }];
        expectedResult = service.addOgrenciToCollectionIfMissing(ogrenciCollection, undefined, null);
        expect(expectedResult).toEqual(ogrenciCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
