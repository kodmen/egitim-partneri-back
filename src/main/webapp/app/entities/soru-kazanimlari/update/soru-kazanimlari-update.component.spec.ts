import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SoruKazanimlariService } from '../service/soru-kazanimlari.service';
import { ISoruKazanimlari, SoruKazanimlari } from '../soru-kazanimlari.model';

import { SoruKazanimlariUpdateComponent } from './soru-kazanimlari-update.component';

describe('SoruKazanimlari Management Update Component', () => {
  let comp: SoruKazanimlariUpdateComponent;
  let fixture: ComponentFixture<SoruKazanimlariUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let soruKazanimlariService: SoruKazanimlariService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SoruKazanimlariUpdateComponent],
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
      .overrideTemplate(SoruKazanimlariUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SoruKazanimlariUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    soruKazanimlariService = TestBed.inject(SoruKazanimlariService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const soruKazanimlari: ISoruKazanimlari = { id: 456 };

      activatedRoute.data = of({ soruKazanimlari });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(soruKazanimlari));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SoruKazanimlari>>();
      const soruKazanimlari = { id: 123 };
      jest.spyOn(soruKazanimlariService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ soruKazanimlari });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: soruKazanimlari }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(soruKazanimlariService.update).toHaveBeenCalledWith(soruKazanimlari);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SoruKazanimlari>>();
      const soruKazanimlari = new SoruKazanimlari();
      jest.spyOn(soruKazanimlariService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ soruKazanimlari });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: soruKazanimlari }));
      saveSubject.complete();

      // THEN
      expect(soruKazanimlariService.create).toHaveBeenCalledWith(soruKazanimlari);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SoruKazanimlari>>();
      const soruKazanimlari = { id: 123 };
      jest.spyOn(soruKazanimlariService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ soruKazanimlari });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(soruKazanimlariService.update).toHaveBeenCalledWith(soruKazanimlari);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
