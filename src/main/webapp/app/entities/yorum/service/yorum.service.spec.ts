import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IYorum, Yorum } from '../yorum.model';

import { YorumService } from './yorum.service';

describe('Yorum Service', () => {
  let service: YorumService;
  let httpMock: HttpTestingController;
  let elemDefault: IYorum;
  let expectedResult: IYorum | IYorum[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(YorumService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      yazi: 'AAAAAAA',
      date: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          date: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Yorum', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          date: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.create(new Yorum()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Yorum', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          yazi: 'BBBBBB',
          date: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Yorum', () => {
      const patchObject = Object.assign(
        {
          yazi: 'BBBBBB',
        },
        new Yorum()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Yorum', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          yazi: 'BBBBBB',
          date: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Yorum', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addYorumToCollectionIfMissing', () => {
      it('should add a Yorum to an empty array', () => {
        const yorum: IYorum = { id: 123 };
        expectedResult = service.addYorumToCollectionIfMissing([], yorum);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(yorum);
      });

      it('should not add a Yorum to an array that contains it', () => {
        const yorum: IYorum = { id: 123 };
        const yorumCollection: IYorum[] = [
          {
            ...yorum,
          },
          { id: 456 },
        ];
        expectedResult = service.addYorumToCollectionIfMissing(yorumCollection, yorum);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Yorum to an array that doesn't contain it", () => {
        const yorum: IYorum = { id: 123 };
        const yorumCollection: IYorum[] = [{ id: 456 }];
        expectedResult = service.addYorumToCollectionIfMissing(yorumCollection, yorum);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(yorum);
      });

      it('should add only unique Yorum to an array', () => {
        const yorumArray: IYorum[] = [{ id: 123 }, { id: 456 }, { id: 54895 }];
        const yorumCollection: IYorum[] = [{ id: 123 }];
        expectedResult = service.addYorumToCollectionIfMissing(yorumCollection, ...yorumArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const yorum: IYorum = { id: 123 };
        const yorum2: IYorum = { id: 456 };
        expectedResult = service.addYorumToCollectionIfMissing([], yorum, yorum2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(yorum);
        expect(expectedResult).toContain(yorum2);
      });

      it('should accept null and undefined values', () => {
        const yorum: IYorum = { id: 123 };
        expectedResult = service.addYorumToCollectionIfMissing([], null, yorum, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(yorum);
      });

      it('should return initial array if no Yorum is added', () => {
        const yorumCollection: IYorum[] = [{ id: 123 }];
        expectedResult = service.addYorumToCollectionIfMissing(yorumCollection, undefined, null);
        expect(expectedResult).toEqual(yorumCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
