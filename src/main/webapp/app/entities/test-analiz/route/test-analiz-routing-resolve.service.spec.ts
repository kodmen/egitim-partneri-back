import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITestAnaliz, TestAnaliz } from '../test-analiz.model';
import { TestAnalizService } from '../service/test-analiz.service';

import { TestAnalizRoutingResolveService } from './test-analiz-routing-resolve.service';

describe('TestAnaliz routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TestAnalizRoutingResolveService;
  let service: TestAnalizService;
  let resultTestAnaliz: ITestAnaliz | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(TestAnalizRoutingResolveService);
    service = TestBed.inject(TestAnalizService);
    resultTestAnaliz = undefined;
  });

  describe('resolve', () => {
    it('should return ITestAnaliz returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTestAnaliz = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTestAnaliz).toEqual({ id: 123 });
    });

    it('should return new ITestAnaliz if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTestAnaliz = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTestAnaliz).toEqual(new TestAnaliz());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TestAnaliz })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTestAnaliz = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTestAnaliz).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
