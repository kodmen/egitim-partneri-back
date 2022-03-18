import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IImageModel, ImageModel } from '../image-model.model';

import { ImageModelService } from './image-model.service';

describe('ImageModel Service', () => {
  let service: ImageModelService;
  let httpMock: HttpTestingController;
  let elemDefault: IImageModel;
  let expectedResult: IImageModel | IImageModel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ImageModelService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      type: 'AAAAAAA',
      imgContentType: 'image/png',
      img: 'AAAAAAA',
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

    it('should create a ImageModel', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ImageModel()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ImageModel', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          type: 'BBBBBB',
          img: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ImageModel', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          type: 'BBBBBB',
          img: 'BBBBBB',
        },
        new ImageModel()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ImageModel', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          type: 'BBBBBB',
          img: 'BBBBBB',
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

    it('should delete a ImageModel', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addImageModelToCollectionIfMissing', () => {
      it('should add a ImageModel to an empty array', () => {
        const imageModel: IImageModel = { id: 123 };
        expectedResult = service.addImageModelToCollectionIfMissing([], imageModel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(imageModel);
      });

      it('should not add a ImageModel to an array that contains it', () => {
        const imageModel: IImageModel = { id: 123 };
        const imageModelCollection: IImageModel[] = [
          {
            ...imageModel,
          },
          { id: 456 },
        ];
        expectedResult = service.addImageModelToCollectionIfMissing(imageModelCollection, imageModel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ImageModel to an array that doesn't contain it", () => {
        const imageModel: IImageModel = { id: 123 };
        const imageModelCollection: IImageModel[] = [{ id: 456 }];
        expectedResult = service.addImageModelToCollectionIfMissing(imageModelCollection, imageModel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(imageModel);
      });

      it('should add only unique ImageModel to an array', () => {
        const imageModelArray: IImageModel[] = [{ id: 123 }, { id: 456 }, { id: 56716 }];
        const imageModelCollection: IImageModel[] = [{ id: 123 }];
        expectedResult = service.addImageModelToCollectionIfMissing(imageModelCollection, ...imageModelArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const imageModel: IImageModel = { id: 123 };
        const imageModel2: IImageModel = { id: 456 };
        expectedResult = service.addImageModelToCollectionIfMissing([], imageModel, imageModel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(imageModel);
        expect(expectedResult).toContain(imageModel2);
      });

      it('should accept null and undefined values', () => {
        const imageModel: IImageModel = { id: 123 };
        expectedResult = service.addImageModelToCollectionIfMissing([], null, imageModel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(imageModel);
      });

      it('should return initial array if no ImageModel is added', () => {
        const imageModelCollection: IImageModel[] = [{ id: 123 }];
        expectedResult = service.addImageModelToCollectionIfMissing(imageModelCollection, undefined, null);
        expect(expectedResult).toEqual(imageModelCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
