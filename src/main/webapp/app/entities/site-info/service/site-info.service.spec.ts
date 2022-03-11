import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISiteInfo, SiteInfo } from '../site-info.model';

import { SiteInfoService } from './site-info.service';

describe('SiteInfo Service', () => {
  let service: SiteInfoService;
  let httpMock: HttpTestingController;
  let elemDefault: ISiteInfo;
  let expectedResult: ISiteInfo | ISiteInfo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SiteInfoService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      baslik: 'AAAAAAA',
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

    it('should create a SiteInfo', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SiteInfo()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SiteInfo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          baslik: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SiteInfo', () => {
      const patchObject = Object.assign(
        {
          baslik: 'BBBBBB',
        },
        new SiteInfo()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SiteInfo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          baslik: 'BBBBBB',
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

    it('should delete a SiteInfo', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSiteInfoToCollectionIfMissing', () => {
      it('should add a SiteInfo to an empty array', () => {
        const siteInfo: ISiteInfo = { id: 123 };
        expectedResult = service.addSiteInfoToCollectionIfMissing([], siteInfo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(siteInfo);
      });

      it('should not add a SiteInfo to an array that contains it', () => {
        const siteInfo: ISiteInfo = { id: 123 };
        const siteInfoCollection: ISiteInfo[] = [
          {
            ...siteInfo,
          },
          { id: 456 },
        ];
        expectedResult = service.addSiteInfoToCollectionIfMissing(siteInfoCollection, siteInfo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SiteInfo to an array that doesn't contain it", () => {
        const siteInfo: ISiteInfo = { id: 123 };
        const siteInfoCollection: ISiteInfo[] = [{ id: 456 }];
        expectedResult = service.addSiteInfoToCollectionIfMissing(siteInfoCollection, siteInfo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(siteInfo);
      });

      it('should add only unique SiteInfo to an array', () => {
        const siteInfoArray: ISiteInfo[] = [{ id: 123 }, { id: 456 }, { id: 60016 }];
        const siteInfoCollection: ISiteInfo[] = [{ id: 123 }];
        expectedResult = service.addSiteInfoToCollectionIfMissing(siteInfoCollection, ...siteInfoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const siteInfo: ISiteInfo = { id: 123 };
        const siteInfo2: ISiteInfo = { id: 456 };
        expectedResult = service.addSiteInfoToCollectionIfMissing([], siteInfo, siteInfo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(siteInfo);
        expect(expectedResult).toContain(siteInfo2);
      });

      it('should accept null and undefined values', () => {
        const siteInfo: ISiteInfo = { id: 123 };
        expectedResult = service.addSiteInfoToCollectionIfMissing([], null, siteInfo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(siteInfo);
      });

      it('should return initial array if no SiteInfo is added', () => {
        const siteInfoCollection: ISiteInfo[] = [{ id: 123 }];
        expectedResult = service.addSiteInfoToCollectionIfMissing(siteInfoCollection, undefined, null);
        expect(expectedResult).toEqual(siteInfoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
