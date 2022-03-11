import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMufredat, Mufredat } from '../mufredat.model';

import { MufredatService } from './mufredat.service';

describe('Mufredat Service', () => {
  let service: MufredatService;
  let httpMock: HttpTestingController;
  let elemDefault: IMufredat;
  let expectedResult: IMufredat | IMufredat[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MufredatService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      mufredatBaslik: 'AAAAAAA',
      toplamSure: 'AAAAAAA',
      bolumSayi: 0,
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

    it('should create a Mufredat', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Mufredat()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Mufredat', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          mufredatBaslik: 'BBBBBB',
          toplamSure: 'BBBBBB',
          bolumSayi: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Mufredat', () => {
      const patchObject = Object.assign(
        {
          toplamSure: 'BBBBBB',
        },
        new Mufredat()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Mufredat', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          mufredatBaslik: 'BBBBBB',
          toplamSure: 'BBBBBB',
          bolumSayi: 1,
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

    it('should delete a Mufredat', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMufredatToCollectionIfMissing', () => {
      it('should add a Mufredat to an empty array', () => {
        const mufredat: IMufredat = { id: 123 };
        expectedResult = service.addMufredatToCollectionIfMissing([], mufredat);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mufredat);
      });

      it('should not add a Mufredat to an array that contains it', () => {
        const mufredat: IMufredat = { id: 123 };
        const mufredatCollection: IMufredat[] = [
          {
            ...mufredat,
          },
          { id: 456 },
        ];
        expectedResult = service.addMufredatToCollectionIfMissing(mufredatCollection, mufredat);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Mufredat to an array that doesn't contain it", () => {
        const mufredat: IMufredat = { id: 123 };
        const mufredatCollection: IMufredat[] = [{ id: 456 }];
        expectedResult = service.addMufredatToCollectionIfMissing(mufredatCollection, mufredat);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mufredat);
      });

      it('should add only unique Mufredat to an array', () => {
        const mufredatArray: IMufredat[] = [{ id: 123 }, { id: 456 }, { id: 43783 }];
        const mufredatCollection: IMufredat[] = [{ id: 123 }];
        expectedResult = service.addMufredatToCollectionIfMissing(mufredatCollection, ...mufredatArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const mufredat: IMufredat = { id: 123 };
        const mufredat2: IMufredat = { id: 456 };
        expectedResult = service.addMufredatToCollectionIfMissing([], mufredat, mufredat2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mufredat);
        expect(expectedResult).toContain(mufredat2);
      });

      it('should accept null and undefined values', () => {
        const mufredat: IMufredat = { id: 123 };
        expectedResult = service.addMufredatToCollectionIfMissing([], null, mufredat, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mufredat);
      });

      it('should return initial array if no Mufredat is added', () => {
        const mufredatCollection: IMufredat[] = [{ id: 123 }];
        expectedResult = service.addMufredatToCollectionIfMissing(mufredatCollection, undefined, null);
        expect(expectedResult).toEqual(mufredatCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
