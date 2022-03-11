import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DersAnalizService } from '../service/ders-analiz.service';
import { IDersAnaliz, DersAnaliz } from '../ders-analiz.model';
import { IBolum } from 'app/entities/bolum/bolum.model';
import { BolumService } from 'app/entities/bolum/service/bolum.service';

import { DersAnalizUpdateComponent } from './ders-analiz-update.component';

describe('DersAnaliz Management Update Component', () => {
  let comp: DersAnalizUpdateComponent;
  let fixture: ComponentFixture<DersAnalizUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dersAnalizService: DersAnalizService;
  let bolumService: BolumService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DersAnalizUpdateComponent],
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
      .overrideTemplate(DersAnalizUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DersAnalizUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dersAnalizService = TestBed.inject(DersAnalizService);
    bolumService = TestBed.inject(BolumService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Bolum query and add missing value', () => {
      const dersAnaliz: IDersAnaliz = { id: 456 };
      const aitOldBolum: IBolum = { id: 58460 };
      dersAnaliz.aitOldBolum = aitOldBolum;

      const bolumCollection: IBolum[] = [{ id: 32398 }];
      jest.spyOn(bolumService, 'query').mockReturnValue(of(new HttpResponse({ body: bolumCollection })));
      const additionalBolums = [aitOldBolum];
      const expectedCollection: IBolum[] = [...additionalBolums, ...bolumCollection];
      jest.spyOn(bolumService, 'addBolumToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dersAnaliz });
      comp.ngOnInit();

      expect(bolumService.query).toHaveBeenCalled();
      expect(bolumService.addBolumToCollectionIfMissing).toHaveBeenCalledWith(bolumCollection, ...additionalBolums);
      expect(comp.bolumsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const dersAnaliz: IDersAnaliz = { id: 456 };
      const aitOldBolum: IBolum = { id: 64034 };
      dersAnaliz.aitOldBolum = aitOldBolum;

      activatedRoute.data = of({ dersAnaliz });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(dersAnaliz));
      expect(comp.bolumsSharedCollection).toContain(aitOldBolum);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DersAnaliz>>();
      const dersAnaliz = { id: 123 };
      jest.spyOn(dersAnalizService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dersAnaliz });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dersAnaliz }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(dersAnalizService.update).toHaveBeenCalledWith(dersAnaliz);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DersAnaliz>>();
      const dersAnaliz = new DersAnaliz();
      jest.spyOn(dersAnalizService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dersAnaliz });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dersAnaliz }));
      saveSubject.complete();

      // THEN
      expect(dersAnalizService.create).toHaveBeenCalledWith(dersAnaliz);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DersAnaliz>>();
      const dersAnaliz = { id: 123 };
      jest.spyOn(dersAnalizService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dersAnaliz });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dersAnalizService.update).toHaveBeenCalledWith(dersAnaliz);
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
});
