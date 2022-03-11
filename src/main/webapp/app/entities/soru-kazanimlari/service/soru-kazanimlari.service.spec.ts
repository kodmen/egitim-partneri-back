import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISoruKazanimlari, SoruKazanimlari } from '../soru-kazanimlari.model';

import { SoruKazanimlariService } from './soru-kazanimlari.service';

describe('SoruKazanimlari Service', () => {
  let service: SoruKazanimlariService;
  let httpMock: HttpTestingController;
  let elemDefault: ISoruKazanimlari;
  let expectedResult: ISoruKazanimlari | ISoruKazanimlari[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SoruKazanimlariService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      kazanim: 'AAAAAAA',
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

    it('should create a SoruKazanimlari', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SoruKazanimlari()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SoruKazanimlari', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          kazanim: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SoruKazanimlari', () => {
      const patchObject = Object.assign({}, new SoruKazanimlari());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SoruKazanimlari', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          kazanim: 'BBBBBB',
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

    it('should delete a SoruKazanimlari', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSoruKazanimlariToCollectionIfMissing', () => {
      it('should add a SoruKazanimlari to an empty array', () => {
        const soruKazanimlari: ISoruKazanimlari = { id: 123 };
        expectedResult = service.addSoruKazanimlariToCollectionIfMissing([], soruKazanimlari);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(soruKazanimlari);
      });

      it('should not add a SoruKazanimlari to an array that contains it', () => {
        const soruKazanimlari: ISoruKazanimlari = { id: 123 };
        const soruKazanimlariCollection: ISoruKazanimlari[] = [
          {
            ...soruKazanimlari,
          },
          { id: 456 },
        ];
        expectedResult = service.addSoruKazanimlariToCollectionIfMissing(soruKazanimlariCollection, soruKazanimlari);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SoruKazanimlari to an array that doesn't contain it", () => {
        const soruKazanimlari: ISoruKazanimlari = { id: 123 };
        const soruKazanimlariCollection: ISoruKazanimlari[] = [{ id: 456 }];
        expectedResult = service.addSoruKazanimlariToCollectionIfMissing(soruKazanimlariCollection, soruKazanimlari);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(soruKazanimlari);
      });

      it('should add only unique SoruKazanimlari to an array', () => {
        const soruKazanimlariArray: ISoruKazanimlari[] = [{ id: 123 }, { id: 456 }, { id: 80636 }];
        const soruKazanimlariCollection: ISoruKazanimlari[] = [{ id: 123 }];
        expectedResult = service.addSoruKazanimlariToCollectionIfMissing(soruKazanimlariCollection, ...soruKazanimlariArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const soruKazanimlari: ISoruKazanimlari = { id: 123 };
        const soruKazanimlari2: ISoruKazanimlari = { id: 456 };
        expectedResult = service.addSoruKazanimlariToCollectionIfMissing([], soruKazanimlari, soruKazanimlari2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(soruKazanimlari);
        expect(expectedResult).toContain(soruKazanimlari2);
      });

      it('should accept null and undefined values', () => {
        const soruKazanimlari: ISoruKazanimlari = { id: 123 };
        expectedResult = service.addSoruKazanimlariToCollectionIfMissing([], null, soruKazanimlari, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(soruKazanimlari);
      });

      it('should return initial array if no SoruKazanimlari is added', () => {
        const soruKazanimlariCollection: ISoruKazanimlari[] = [{ id: 123 }];
        expectedResult = service.addSoruKazanimlariToCollectionIfMissing(soruKazanimlariCollection, undefined, null);
        expect(expectedResult).toEqual(soruKazanimlariCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
