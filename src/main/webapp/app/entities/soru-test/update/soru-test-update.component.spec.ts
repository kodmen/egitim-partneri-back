import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SoruTestService } from '../service/soru-test.service';
import { ISoruTest, SoruTest } from '../soru-test.model';
import { ISoru } from 'app/entities/soru/soru.model';
import { SoruService } from 'app/entities/soru/service/soru.service';
import { IBolum } from 'app/entities/bolum/bolum.model';
import { BolumService } from 'app/entities/bolum/service/bolum.service';

import { SoruTestUpdateComponent } from './soru-test-update.component';

describe('SoruTest Management Update Component', () => {
  let comp: SoruTestUpdateComponent;
  let fixture: ComponentFixture<SoruTestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let soruTestService: SoruTestService;
  let soruService: SoruService;
  let bolumService: BolumService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SoruTestUpdateComponent],
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
      .overrideTemplate(SoruTestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SoruTestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    soruTestService = TestBed.inject(SoruTestService);
    soruService = TestBed.inject(SoruService);
    bolumService = TestBed.inject(BolumService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Soru query and add missing value', () => {
      const soruTest: ISoruTest = { id: 456 };
      const sorulars: ISoru[] = [{ id: 33661 }];
      soruTest.sorulars = sorulars;

      const soruCollection: ISoru[] = [{ id: 2449 }];
      jest.spyOn(soruService, 'query').mockReturnValue(of(new HttpResponse({ body: soruCollection })));
      const additionalSorus = [...sorulars];
      const expectedCollection: ISoru[] = [...additionalSorus, ...soruCollection];
      jest.spyOn(soruService, 'addSoruToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ soruTest });
      comp.ngOnInit();

      expect(soruService.query).toHaveBeenCalled();
      expect(soruService.addSoruToCollectionIfMissing).toHaveBeenCalledWith(soruCollection, ...additionalSorus);
      expect(comp.sorusSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Bolum query and add missing value', () => {
      const soruTest: ISoruTest = { id: 456 };
      const testBolum: IBolum = { id: 53816 };
      soruTest.testBolum = testBolum;

      const bolumCollection: IBolum[] = [{ id: 6520 }];
      jest.spyOn(bolumService, 'query').mockReturnValue(of(new HttpResponse({ body: bolumCollection })));
      const additionalBolums = [testBolum];
      const expectedCollection: IBolum[] = [...additionalBolums, ...bolumCollection];
      jest.spyOn(bolumService, 'addBolumToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ soruTest });
      comp.ngOnInit();

      expect(bolumService.query).toHaveBeenCalled();
      expect(bolumService.addBolumToCollectionIfMissing).toHaveBeenCalledWith(bolumCollection, ...additionalBolums);
      expect(comp.bolumsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const soruTest: ISoruTest = { id: 456 };
      const sorulars: ISoru = { id: 88534 };
      soruTest.sorulars = [sorulars];
      const testBolum: IBolum = { id: 73306 };
      soruTest.testBolum = testBolum;

      activatedRoute.data = of({ soruTest });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(soruTest));
      expect(comp.sorusSharedCollection).toContain(sorulars);
      expect(comp.bolumsSharedCollection).toContain(testBolum);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SoruTest>>();
      const soruTest = { id: 123 };
      jest.spyOn(soruTestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ soruTest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: soruTest }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(soruTestService.update).toHaveBeenCalledWith(soruTest);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SoruTest>>();
      const soruTest = new SoruTest();
      jest.spyOn(soruTestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ soruTest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: soruTest }));
      saveSubject.complete();

      // THEN
      expect(soruTestService.create).toHaveBeenCalledWith(soruTest);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SoruTest>>();
      const soruTest = { id: 123 };
      jest.spyOn(soruTestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ soruTest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(soruTestService.update).toHaveBeenCalledWith(soruTest);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSoruById', () => {
      it('Should return tracked Soru primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSoruById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackBolumById', () => {
      it('Should return tracked Bolum primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBolumById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedSoru', () => {
      it('Should return option if no Soru is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedSoru(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Soru for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedSoru(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Soru is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedSoru(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
