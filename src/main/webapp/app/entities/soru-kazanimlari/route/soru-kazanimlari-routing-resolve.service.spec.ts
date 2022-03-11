import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ISoruKazanimlari, SoruKazanimlari } from '../soru-kazanimlari.model';
import { SoruKazanimlariService } from '../service/soru-kazanimlari.service';

import { SoruKazanimlariRoutingResolveService } from './soru-kazanimlari-routing-resolve.service';

describe('SoruKazanimlari routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SoruKazanimlariRoutingResolveService;
  let service: SoruKazanimlariService;
  let resultSoruKazanimlari: ISoruKazanimlari | undefined;

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
    routingResolveService = TestBed.inject(SoruKazanimlariRoutingResolveService);
    service = TestBed.inject(SoruKazanimlariService);
    resultSoruKazanimlari = undefined;
  });

  describe('resolve', () => {
    it('should return ISoruKazanimlari returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSoruKazanimlari = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSoruKazanimlari).toEqual({ id: 123 });
    });

    it('should return new ISoruKazanimlari if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSoruKazanimlari = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSoruKazanimlari).toEqual(new SoruKazanimlari());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SoruKazanimlari })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSoruKazanimlari = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSoruKazanimlari).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
