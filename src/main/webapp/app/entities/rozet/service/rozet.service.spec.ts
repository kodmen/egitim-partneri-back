import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRozet, Rozet } from '../rozet.model';

import { RozetService } from './rozet.service';

describe('Rozet Service', () => {
  let service: RozetService;
  let httpMock: HttpTestingController;
  let elemDefault: IRozet;
  let expectedResult: IRozet | IRozet[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RozetService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      rozetIsmi: 'AAAAAAA',
      rozetResimContentType: 'image/png',
      rozetResim: 'AAAAAAA',
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

    it('should create a Rozet', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Rozet()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Rozet', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          rozetIsmi: 'BBBBBB',
          rozetResim: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Rozet', () => {
      const patchObject = Object.assign(
        {
          rozetIsmi: 'BBBBBB',
          rozetResim: 'BBBBBB',
        },
        new Rozet()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Rozet', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          rozetIsmi: 'BBBBBB',
          rozetResim: 'BBBBBB',
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

    it('should delete a Rozet', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRozetToCollectionIfMissing', () => {
      it('should add a Rozet to an empty array', () => {
        const rozet: IRozet = { id: 123 };
        expectedResult = service.addRozetToCollectionIfMissing([], rozet);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rozet);
      });

      it('should not add a Rozet to an array that contains it', () => {
        const rozet: IRozet = { id: 123 };
        const rozetCollection: IRozet[] = [
          {
            ...rozet,
          },
          { id: 456 },
        ];
        expectedResult = service.addRozetToCollectionIfMissing(rozetCollection, rozet);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Rozet to an array that doesn't contain it", () => {
        const rozet: IRozet = { id: 123 };
        const rozetCollection: IRozet[] = [{ id: 456 }];
        expectedResult = service.addRozetToCollectionIfMissing(rozetCollection, rozet);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rozet);
      });

      it('should add only unique Rozet to an array', () => {
        const rozetArray: IRozet[] = [{ id: 123 }, { id: 456 }, { id: 7921 }];
        const rozetCollection: IRozet[] = [{ id: 123 }];
        expectedResult = service.addRozetToCollectionIfMissing(rozetCollection, ...rozetArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rozet: IRozet = { id: 123 };
        const rozet2: IRozet = { id: 456 };
        expectedResult = service.addRozetToCollectionIfMissing([], rozet, rozet2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rozet);
        expect(expectedResult).toContain(rozet2);
      });

      it('should accept null and undefined values', () => {
        const rozet: IRozet = { id: 123 };
        expectedResult = service.addRozetToCollectionIfMissing([], null, rozet, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rozet);
      });

      it('should return initial array if no Rozet is added', () => {
        const rozetCollection: IRozet[] = [{ id: 123 }];
        expectedResult = service.addRozetToCollectionIfMissing(rozetCollection, undefined, null);
        expect(expectedResult).toEqual(rozetCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
