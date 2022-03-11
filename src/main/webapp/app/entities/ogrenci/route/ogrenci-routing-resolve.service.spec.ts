import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IOgrenci, Ogrenci } from '../ogrenci.model';
import { OgrenciService } from '../service/ogrenci.service';

import { OgrenciRoutingResolveService } from './ogrenci-routing-resolve.service';

describe('Ogrenci routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: OgrenciRoutingResolveService;
  let service: OgrenciService;
  let resultOgrenci: IOgrenci | undefined;

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
    routingResolveService = TestBed.inject(OgrenciRoutingResolveService);
    service = TestBed.inject(OgrenciService);
    resultOgrenci = undefined;
  });

  describe('resolve', () => {
    it('should return IOgrenci returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOgrenci = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOgrenci).toEqual({ id: 123 });
    });

    it('should return new IOgrenci if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOgrenci = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultOgrenci).toEqual(new Ogrenci());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Ogrenci })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOgrenci = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOgrenci).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
