import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TestAnalizService } from '../service/test-analiz.service';
import { ITestAnaliz, TestAnaliz } from '../test-analiz.model';
import { IDersAnaliz } from 'app/entities/ders-analiz/ders-analiz.model';
import { DersAnalizService } from 'app/entities/ders-analiz/service/ders-analiz.service';

import { TestAnalizUpdateComponent } from './test-analiz-update.component';

describe('TestAnaliz Management Update Component', () => {
  let comp: TestAnalizUpdateComponent;
  let fixture: ComponentFixture<TestAnalizUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let testAnalizService: TestAnalizService;
  let dersAnalizService: DersAnalizService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TestAnalizUpdateComponent],
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
      .overrideTemplate(TestAnalizUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TestAnalizUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    testAnalizService = TestBed.inject(TestAnalizService);
    dersAnalizService = TestBed.inject(DersAnalizService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call DersAnaliz query and add missing value', () => {
      const testAnaliz: ITestAnaliz = { id: 456 };
      const dersAnaliz: IDersAnaliz = { id: 49219 };
      testAnaliz.dersAnaliz = dersAnaliz;

      const dersAnalizCollection: IDersAnaliz[] = [{ id: 19860 }];
      jest.spyOn(dersAnalizService, 'query').mockReturnValue(of(new HttpResponse({ body: dersAnalizCollection })));
      const additionalDersAnalizs = [dersAnaliz];
      const expectedCollection: IDersAnaliz[] = [...additionalDersAnalizs, ...dersAnalizCollection];
      jest.spyOn(dersAnalizService, 'addDersAnalizToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ testAnaliz });
      comp.ngOnInit();

      expect(dersAnalizService.query).toHaveBeenCalled();
      expect(dersAnalizService.addDersAnalizToCollectionIfMissing).toHaveBeenCalledWith(dersAnalizCollection, ...additionalDersAnalizs);
      expect(comp.dersAnalizsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const testAnaliz: ITestAnaliz = { id: 456 };
      const dersAnaliz: IDersAnaliz = { id: 95956 };
      testAnaliz.dersAnaliz = dersAnaliz;

      activatedRoute.data = of({ testAnaliz });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(testAnaliz));
      expect(comp.dersAnalizsSharedCollection).toContain(dersAnaliz);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TestAnaliz>>();
      const testAnaliz = { id: 123 };
      jest.spyOn(testAnalizService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testAnaliz });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: testAnaliz }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(testAnalizService.update).toHaveBeenCalledWith(testAnaliz);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TestAnaliz>>();
      const testAnaliz = new TestAnaliz();
      jest.spyOn(testAnalizService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testAnaliz });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: testAnaliz }));
      saveSubject.complete();

      // THEN
      expect(testAnalizService.create).toHaveBeenCalledWith(testAnaliz);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TestAnaliz>>();
      const testAnaliz = { id: 123 };
      jest.spyOn(testAnalizService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testAnaliz });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(testAnalizService.update).toHaveBeenCalledWith(testAnaliz);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDersAnalizById', () => {
      it('Should return tracked DersAnaliz primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDersAnalizById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
