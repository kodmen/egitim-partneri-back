import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEntry, Entry } from '../entry.model';

import { EntryService } from './entry.service';

describe('Entry Service', () => {
  let service: EntryService;
  let httpMock: HttpTestingController;
  let elemDefault: IEntry;
  let expectedResult: IEntry | IEntry[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EntryService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      title: 'AAAAAAA',
      content: 'AAAAAAA',
      date: currentDate,
      fotoContentType: 'image/png',
      foto: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Entry', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.create(new Entry()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Entry', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          title: 'BBBBBB',
          content: 'BBBBBB',
          date: currentDate.format(DATE_TIME_FORMAT),
          foto: 'BBBBBB',
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

    it('should partial update a Entry', () => {
      const patchObject = Object.assign({}, new Entry());

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

    it('should return a list of Entry', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          title: 'BBBBBB',
          content: 'BBBBBB',
          date: currentDate.format(DATE_TIME_FORMAT),
          foto: 'BBBBBB',
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

    it('should delete a Entry', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEntryToCollectionIfMissing', () => {
      it('should add a Entry to an empty array', () => {
        const entry: IEntry = { id: 123 };
        expectedResult = service.addEntryToCollectionIfMissing([], entry);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(entry);
      });

      it('should not add a Entry to an array that contains it', () => {
        const entry: IEntry = { id: 123 };
        const entryCollection: IEntry[] = [
          {
            ...entry,
          },
          { id: 456 },
        ];
        expectedResult = service.addEntryToCollectionIfMissing(entryCollection, entry);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Entry to an array that doesn't contain it", () => {
        const entry: IEntry = { id: 123 };
        const entryCollection: IEntry[] = [{ id: 456 }];
        expectedResult = service.addEntryToCollectionIfMissing(entryCollection, entry);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(entry);
      });

      it('should add only unique Entry to an array', () => {
        const entryArray: IEntry[] = [{ id: 123 }, { id: 456 }, { id: 2534 }];
        const entryCollection: IEntry[] = [{ id: 123 }];
        expectedResult = service.addEntryToCollectionIfMissing(entryCollection, ...entryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const entry: IEntry = { id: 123 };
        const entry2: IEntry = { id: 456 };
        expectedResult = service.addEntryToCollectionIfMissing([], entry, entry2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(entry);
        expect(expectedResult).toContain(entry2);
      });

      it('should accept null and undefined values', () => {
        const entry: IEntry = { id: 123 };
        expectedResult = service.addEntryToCollectionIfMissing([], null, entry, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(entry);
      });

      it('should return initial array if no Entry is added', () => {
        const entryCollection: IEntry[] = [{ id: 123 }];
        expectedResult = service.addEntryToCollectionIfMissing(entryCollection, undefined, null);
        expect(expectedResult).toEqual(entryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
