import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IDersAnaliz, DersAnaliz } from '../ders-analiz.model';
import { DersAnalizService } from '../service/ders-analiz.service';

import { DersAnalizRoutingResolveService } from './ders-analiz-routing-resolve.service';

describe('DersAnaliz routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DersAnalizRoutingResolveService;
  let service: DersAnalizService;
  let resultDersAnaliz: IDersAnaliz | undefined;

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
    routingResolveService = TestBed.inject(DersAnalizRoutingResolveService);
    service = TestBed.inject(DersAnalizService);
    resultDersAnaliz = undefined;
  });

  describe('resolve', () => {
    it('should return IDersAnaliz returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDersAnaliz = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDersAnaliz).toEqual({ id: 123 });
    });

    it('should return new IDersAnaliz if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDersAnaliz = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDersAnaliz).toEqual(new DersAnaliz());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DersAnaliz })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDersAnaliz = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDersAnaliz).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
