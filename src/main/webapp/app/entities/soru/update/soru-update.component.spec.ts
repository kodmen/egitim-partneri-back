import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SoruService } from '../service/soru.service';
import { ISoru, Soru } from '../soru.model';
import { ISoruKazanimlari } from 'app/entities/soru-kazanimlari/soru-kazanimlari.model';
import { SoruKazanimlariService } from 'app/entities/soru-kazanimlari/service/soru-kazanimlari.service';

import { SoruUpdateComponent } from './soru-update.component';

describe('Soru Management Update Component', () => {
  let comp: SoruUpdateComponent;
  let fixture: ComponentFixture<SoruUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let soruService: SoruService;
  let soruKazanimlariService: SoruKazanimlariService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SoruUpdateComponent],
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
      .overrideTemplate(SoruUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SoruUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    soruService = TestBed.inject(SoruService);
    soruKazanimlariService = TestBed.inject(SoruKazanimlariService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SoruKazanimlari query and add missing value', () => {
      const soru: ISoru = { id: 456 };
      const kazanimlars: ISoruKazanimlari[] = [{ id: 9544 }];
      soru.kazanimlars = kazanimlars;

      const soruKazanimlariCollection: ISoruKazanimlari[] = [{ id: 56696 }];
      jest.spyOn(soruKazanimlariService, 'query').mockReturnValue(of(new HttpResponse({ body: soruKazanimlariCollection })));
      const additionalSoruKazanimlaris = [...kazanimlars];
      const expectedCollection: ISoruKazanimlari[] = [...additionalSoruKazanimlaris, ...soruKazanimlariCollection];
      jest.spyOn(soruKazanimlariService, 'addSoruKazanimlariToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ soru });
      comp.ngOnInit();

      expect(soruKazanimlariService.query).toHaveBeenCalled();
      expect(soruKazanimlariService.addSoruKazanimlariToCollectionIfMissing).toHaveBeenCalledWith(
        soruKazanimlariCollection,
        ...additionalSoruKazanimlaris
      );
      expect(comp.soruKazanimlarisSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const soru: ISoru = { id: 456 };
      const kazanimlars: ISoruKazanimlari = { id: 76405 };
      soru.kazanimlars = [kazanimlars];

      activatedRoute.data = of({ soru });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(soru));
      expect(comp.soruKazanimlarisSharedCollection).toContain(kazanimlars);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Soru>>();
      const soru = { id: 123 };
      jest.spyOn(soruService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ soru });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: soru }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(soruService.update).toHaveBeenCalledWith(soru);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Soru>>();
      const soru = new Soru();
      jest.spyOn(soruService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ soru });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: soru }));
      saveSubject.complete();

      // THEN
      expect(soruService.create).toHaveBeenCalledWith(soru);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Soru>>();
      const soru = { id: 123 };
      jest.spyOn(soruService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ soru });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(soruService.update).toHaveBeenCalledWith(soru);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSoruKazanimlariById', () => {
      it('Should return tracked SoruKazanimlari primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSoruKazanimlariById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedSoruKazanimlari', () => {
      it('Should return option if no SoruKazanimlari is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedSoruKazanimlari(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected SoruKazanimlari for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedSoruKazanimlari(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this SoruKazanimlari is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedSoruKazanimlari(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
