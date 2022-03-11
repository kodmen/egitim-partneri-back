import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BolumService } from '../service/bolum.service';
import { IBolum, Bolum } from '../bolum.model';

import { BolumUpdateComponent } from './bolum-update.component';

describe('Bolum Management Update Component', () => {
  let comp: BolumUpdateComponent;
  let fixture: ComponentFixture<BolumUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bolumService: BolumService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BolumUpdateComponent],
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
      .overrideTemplate(BolumUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BolumUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bolumService = TestBed.inject(BolumService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const bolum: IBolum = { id: 456 };

      activatedRoute.data = of({ bolum });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(bolum));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bolum>>();
      const bolum = { id: 123 };
      jest.spyOn(bolumService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bolum });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bolum }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(bolumService.update).toHaveBeenCalledWith(bolum);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bolum>>();
      const bolum = new Bolum();
      jest.spyOn(bolumService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bolum });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bolum }));
      saveSubject.complete();

      // THEN
      expect(bolumService.create).toHaveBeenCalledWith(bolum);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bolum>>();
      const bolum = { id: 123 };
      jest.spyOn(bolumService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bolum });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bolumService.update).toHaveBeenCalledWith(bolum);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
