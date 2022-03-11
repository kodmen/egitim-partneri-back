import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RozetService } from '../service/rozet.service';
import { IRozet, Rozet } from '../rozet.model';

import { RozetUpdateComponent } from './rozet-update.component';

describe('Rozet Management Update Component', () => {
  let comp: RozetUpdateComponent;
  let fixture: ComponentFixture<RozetUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rozetService: RozetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RozetUpdateComponent],
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
      .overrideTemplate(RozetUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RozetUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rozetService = TestBed.inject(RozetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const rozet: IRozet = { id: 456 };

      activatedRoute.data = of({ rozet });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(rozet));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Rozet>>();
      const rozet = { id: 123 };
      jest.spyOn(rozetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rozet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rozet }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(rozetService.update).toHaveBeenCalledWith(rozet);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Rozet>>();
      const rozet = new Rozet();
      jest.spyOn(rozetService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rozet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rozet }));
      saveSubject.complete();

      // THEN
      expect(rozetService.create).toHaveBeenCalledWith(rozet);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Rozet>>();
      const rozet = { id: 123 };
      jest.spyOn(rozetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rozet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rozetService.update).toHaveBeenCalledWith(rozet);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
