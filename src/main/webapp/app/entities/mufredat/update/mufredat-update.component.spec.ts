import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MufredatService } from '../service/mufredat.service';
import { IMufredat, Mufredat } from '../mufredat.model';
import { IBolum } from 'app/entities/bolum/bolum.model';
import { BolumService } from 'app/entities/bolum/service/bolum.service';

import { MufredatUpdateComponent } from './mufredat-update.component';

describe('Mufredat Management Update Component', () => {
  let comp: MufredatUpdateComponent;
  let fixture: ComponentFixture<MufredatUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let mufredatService: MufredatService;
  let bolumService: BolumService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MufredatUpdateComponent],
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
      .overrideTemplate(MufredatUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MufredatUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    mufredatService = TestBed.inject(MufredatService);
    bolumService = TestBed.inject(BolumService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Bolum query and add missing value', () => {
      const mufredat: IMufredat = { id: 456 };
      const bolumlers: IBolum[] = [{ id: 94919 }];
      mufredat.bolumlers = bolumlers;

      const bolumCollection: IBolum[] = [{ id: 4622 }];
      jest.spyOn(bolumService, 'query').mockReturnValue(of(new HttpResponse({ body: bolumCollection })));
      const additionalBolums = [...bolumlers];
      const expectedCollection: IBolum[] = [...additionalBolums, ...bolumCollection];
      jest.spyOn(bolumService, 'addBolumToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ mufredat });
      comp.ngOnInit();

      expect(bolumService.query).toHaveBeenCalled();
      expect(bolumService.addBolumToCollectionIfMissing).toHaveBeenCalledWith(bolumCollection, ...additionalBolums);
      expect(comp.bolumsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const mufredat: IMufredat = { id: 456 };
      const bolumlers: IBolum = { id: 83842 };
      mufredat.bolumlers = [bolumlers];

      activatedRoute.data = of({ mufredat });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(mufredat));
      expect(comp.bolumsSharedCollection).toContain(bolumlers);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Mufredat>>();
      const mufredat = { id: 123 };
      jest.spyOn(mufredatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mufredat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mufredat }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(mufredatService.update).toHaveBeenCalledWith(mufredat);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Mufredat>>();
      const mufredat = new Mufredat();
      jest.spyOn(mufredatService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mufredat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mufredat }));
      saveSubject.complete();

      // THEN
      expect(mufredatService.create).toHaveBeenCalledWith(mufredat);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Mufredat>>();
      const mufredat = { id: 123 };
      jest.spyOn(mufredatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mufredat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(mufredatService.update).toHaveBeenCalledWith(mufredat);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackBolumById', () => {
      it('Should return tracked Bolum primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBolumById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedBolum', () => {
      it('Should return option if no Bolum is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedBolum(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Bolum for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedBolum(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Bolum is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedBolum(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
