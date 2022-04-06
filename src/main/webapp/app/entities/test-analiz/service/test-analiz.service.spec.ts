import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITestAnaliz, TestAnaliz } from '../test-analiz.model';

import { TestAnalizService } from './test-analiz.service';

describe('TestAnaliz Service', () => {
  let service: TestAnalizService;
  let httpMock: HttpTestingController;
  let elemDefault: ITestAnaliz;
  let expectedResult: ITestAnaliz | ITestAnaliz[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TestAnalizService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      dogru: 0,
      yanlis: 0,
      bos: 0,
      net: 0,
      tamamlandi: false,
      testId: 0,
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

    it('should create a TestAnaliz', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TestAnaliz()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TestAnaliz', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dogru: 1,
          yanlis: 1,
          bos: 1,
          net: 1,
          tamamlandi: true,
          testId: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TestAnaliz', () => {
      const patchObject = Object.assign(
        {
          yanlis: 1,
          net: 1,
          tamamlandi: true,
          testId: 1,
        },
        new TestAnaliz()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TestAnaliz', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dogru: 1,
          yanlis: 1,
          bos: 1,
          net: 1,
          tamamlandi: true,
          testId: 1,
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

    it('should delete a TestAnaliz', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTestAnalizToCollectionIfMissing', () => {
      it('should add a TestAnaliz to an empty array', () => {
        const testAnaliz: ITestAnaliz = { id: 123 };
        expectedResult = service.addTestAnalizToCollectionIfMissing([], testAnaliz);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(testAnaliz);
      });

      it('should not add a TestAnaliz to an array that contains it', () => {
        const testAnaliz: ITestAnaliz = { id: 123 };
        const testAnalizCollection: ITestAnaliz[] = [
          {
            ...testAnaliz,
          },
          { id: 456 },
        ];
        expectedResult = service.addTestAnalizToCollectionIfMissing(testAnalizCollection, testAnaliz);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TestAnaliz to an array that doesn't contain it", () => {
        const testAnaliz: ITestAnaliz = { id: 123 };
        const testAnalizCollection: ITestAnaliz[] = [{ id: 456 }];
        expectedResult = service.addTestAnalizToCollectionIfMissing(testAnalizCollection, testAnaliz);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(testAnaliz);
      });

      it('should add only unique TestAnaliz to an array', () => {
        const testAnalizArray: ITestAnaliz[] = [{ id: 123 }, { id: 456 }, { id: 68156 }];
        const testAnalizCollection: ITestAnaliz[] = [{ id: 123 }];
        expectedResult = service.addTestAnalizToCollectionIfMissing(testAnalizCollection, ...testAnalizArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const testAnaliz: ITestAnaliz = { id: 123 };
        const testAnaliz2: ITestAnaliz = { id: 456 };
        expectedResult = service.addTestAnalizToCollectionIfMissing([], testAnaliz, testAnaliz2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(testAnaliz);
        expect(expectedResult).toContain(testAnaliz2);
      });

      it('should accept null and undefined values', () => {
        const testAnaliz: ITestAnaliz = { id: 123 };
        expectedResult = service.addTestAnalizToCollectionIfMissing([], null, testAnaliz, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(testAnaliz);
      });

      it('should return initial array if no TestAnaliz is added', () => {
        const testAnalizCollection: ITestAnaliz[] = [{ id: 123 }];
        expectedResult = service.addTestAnalizToCollectionIfMissing(testAnalizCollection, undefined, null);
        expect(expectedResult).toEqual(testAnalizCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
