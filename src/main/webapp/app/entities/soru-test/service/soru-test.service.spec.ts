import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISoruTest, SoruTest } from '../soru-test.model';

import { SoruTestService } from './soru-test.service';

describe('SoruTest Service', () => {
  let service: SoruTestService;
  let httpMock: HttpTestingController;
  let elemDefault: ISoruTest;
  let expectedResult: ISoruTest | ISoruTest[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SoruTestService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      tesBaslik: 'AAAAAAA',
      testPdf: 'AAAAAAA',
      testFotoContentType: 'image/png',
      testFoto: 'AAAAAAA',
      cevaplar: 'AAAAAAA',
      soruPdfFileContentType: 'image/png',
      soruPdfFile: 'AAAAAAA',
      soruSayisi: 0,
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

    it('should create a SoruTest', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SoruTest()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SoruTest', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          tesBaslik: 'BBBBBB',
          testPdf: 'BBBBBB',
          testFoto: 'BBBBBB',
          cevaplar: 'BBBBBB',
          soruPdfFile: 'BBBBBB',
          soruSayisi: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SoruTest', () => {
      const patchObject = Object.assign(
        {
          tesBaslik: 'BBBBBB',
          testPdf: 'BBBBBB',
          testFoto: 'BBBBBB',
          soruPdfFile: 'BBBBBB',
        },
        new SoruTest()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SoruTest', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          tesBaslik: 'BBBBBB',
          testPdf: 'BBBBBB',
          testFoto: 'BBBBBB',
          cevaplar: 'BBBBBB',
          soruPdfFile: 'BBBBBB',
          soruSayisi: 1,
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

    it('should delete a SoruTest', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSoruTestToCollectionIfMissing', () => {
      it('should add a SoruTest to an empty array', () => {
        const soruTest: ISoruTest = { id: 123 };
        expectedResult = service.addSoruTestToCollectionIfMissing([], soruTest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(soruTest);
      });

      it('should not add a SoruTest to an array that contains it', () => {
        const soruTest: ISoruTest = { id: 123 };
        const soruTestCollection: ISoruTest[] = [
          {
            ...soruTest,
          },
          { id: 456 },
        ];
        expectedResult = service.addSoruTestToCollectionIfMissing(soruTestCollection, soruTest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SoruTest to an array that doesn't contain it", () => {
        const soruTest: ISoruTest = { id: 123 };
        const soruTestCollection: ISoruTest[] = [{ id: 456 }];
        expectedResult = service.addSoruTestToCollectionIfMissing(soruTestCollection, soruTest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(soruTest);
      });

      it('should add only unique SoruTest to an array', () => {
        const soruTestArray: ISoruTest[] = [{ id: 123 }, { id: 456 }, { id: 95717 }];
        const soruTestCollection: ISoruTest[] = [{ id: 123 }];
        expectedResult = service.addSoruTestToCollectionIfMissing(soruTestCollection, ...soruTestArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const soruTest: ISoruTest = { id: 123 };
        const soruTest2: ISoruTest = { id: 456 };
        expectedResult = service.addSoruTestToCollectionIfMissing([], soruTest, soruTest2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(soruTest);
        expect(expectedResult).toContain(soruTest2);
      });

      it('should accept null and undefined values', () => {
        const soruTest: ISoruTest = { id: 123 };
        expectedResult = service.addSoruTestToCollectionIfMissing([], null, soruTest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(soruTest);
      });

      it('should return initial array if no SoruTest is added', () => {
        const soruTestCollection: ISoruTest[] = [{ id: 123 }];
        expectedResult = service.addSoruTestToCollectionIfMissing(soruTestCollection, undefined, null);
        expect(expectedResult).toEqual(soruTestCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
