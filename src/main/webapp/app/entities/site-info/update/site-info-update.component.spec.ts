import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SiteInfoService } from '../service/site-info.service';
import { ISiteInfo, SiteInfo } from '../site-info.model';

import { SiteInfoUpdateComponent } from './site-info-update.component';

describe('SiteInfo Management Update Component', () => {
  let comp: SiteInfoUpdateComponent;
  let fixture: ComponentFixture<SiteInfoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let siteInfoService: SiteInfoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SiteInfoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SiteInfoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SiteInfoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    siteInfoService = TestBed.inject(SiteInfoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const siteInfo: ISiteInfo = { id: 456 };

      activatedRoute.data = of({ siteInfo });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(siteInfo));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SiteInfo>>();
      const siteInfo = { id: 123 };
      jest.spyOn(siteInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ siteInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: siteInfo }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(siteInfoService.update).toHaveBeenCalledWith(siteInfo);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SiteInfo>>();
      const siteInfo = new SiteInfo();
      jest.spyOn(siteInfoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ siteInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: siteInfo }));
      saveSubject.complete();

      // THEN
      expect(siteInfoService.create).toHaveBeenCalledWith(siteInfo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SiteInfo>>();
      const siteInfo = { id: 123 };
      jest.spyOn(siteInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ siteInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(siteInfoService.update).toHaveBeenCalledWith(siteInfo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
