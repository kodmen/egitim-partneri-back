import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DersService } from '../service/ders.service';
import { IDers, Ders } from '../ders.model';
import { IMufredat } from 'app/entities/mufredat/mufredat.model';
import { MufredatService } from 'app/entities/mufredat/service/mufredat.service';
import { IForm } from 'app/entities/form/form.model';
import { FormService } from 'app/entities/form/service/form.service';
import { IOgretmen } from 'app/entities/ogretmen/ogretmen.model';
import { OgretmenService } from 'app/entities/ogretmen/service/ogretmen.service';

import { DersUpdateComponent } from './ders-update.component';

describe('Ders Management Update Component', () => {
  let comp: DersUpdateComponent;
  let fixture: ComponentFixture<DersUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dersService: DersService;
  let mufredatService: MufredatService;
  let formService: FormService;
  let ogretmenService: OgretmenService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DersUpdateComponent],
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
      .overrideTemplate(DersUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DersUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dersService = TestBed.inject(DersService);
    mufredatService = TestBed.inject(MufredatService);
    formService = TestBed.inject(FormService);
    ogretmenService = TestBed.inject(OgretmenService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call dersMufredat query and add missing value', () => {
      const ders: IDers = { id: 456 };
      const dersMufredat: IMufredat = { id: 89658 };
      ders.dersMufredat = dersMufredat;

      const dersMufredatCollection: IMufredat[] = [{ id: 61572 }];
      jest.spyOn(mufredatService, 'query').mockReturnValue(of(new HttpResponse({ body: dersMufredatCollection })));
      const expectedCollection: IMufredat[] = [dersMufredat, ...dersMufredatCollection];
      jest.spyOn(mufredatService, 'addMufredatToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ders });
      comp.ngOnInit();

      expect(mufredatService.query).toHaveBeenCalled();
      expect(mufredatService.addMufredatToCollectionIfMissing).toHaveBeenCalledWith(dersMufredatCollection, dersMufredat);
      expect(comp.dersMufredatsCollection).toEqual(expectedCollection);
    });

    it('Should call dersForm query and add missing value', () => {
      const ders: IDers = { id: 456 };
      const dersForm: IForm = { id: 67309 };
      ders.dersForm = dersForm;

      const dersFormCollection: IForm[] = [{ id: 6849 }];
      jest.spyOn(formService, 'query').mockReturnValue(of(new HttpResponse({ body: dersFormCollection })));
      const expectedCollection: IForm[] = [dersForm, ...dersFormCollection];
      jest.spyOn(formService, 'addFormToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ders });
      comp.ngOnInit();

      expect(formService.query).toHaveBeenCalled();
      expect(formService.addFormToCollectionIfMissing).toHaveBeenCalledWith(dersFormCollection, dersForm);
      expect(comp.dersFormsCollection).toEqual(expectedCollection);
    });

    it('Should call Ogretmen query and add missing value', () => {
      const ders: IDers = { id: 456 };
      const dersOgretmeni: IOgretmen = { id: 48375 };
      ders.dersOgretmeni = dersOgretmeni;

      const ogretmenCollection: IOgretmen[] = [{ id: 27177 }];
      jest.spyOn(ogretmenService, 'query').mockReturnValue(of(new HttpResponse({ body: ogretmenCollection })));
      const additionalOgretmen = [dersOgretmeni];
      const expectedCollection: IOgretmen[] = [...additionalOgretmen, ...ogretmenCollection];
      jest.spyOn(ogretmenService, 'addOgretmenToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ders });
      comp.ngOnInit();

      expect(ogretmenService.query).toHaveBeenCalled();
      expect(ogretmenService.addOgretmenToCollectionIfMissing).toHaveBeenCalledWith(ogretmenCollection, ...additionalOgretmen);
      expect(comp.ogretmenSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ders: IDers = { id: 456 };
      const dersMufredat: IMufredat = { id: 84896 };
      ders.dersMufredat = dersMufredat;
      const dersForm: IForm = { id: 82623 };
      ders.dersForm = dersForm;
      const dersOgretmeni: IOgretmen = { id: 47386 };
      ders.dersOgretmeni = dersOgretmeni;

      activatedRoute.data = of({ ders });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(ders));
      expect(comp.dersMufredatsCollection).toContain(dersMufredat);
      expect(comp.dersFormsCollection).toContain(dersForm);
      expect(comp.ogretmenSharedCollection).toContain(dersOgretmeni);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ders>>();
      const ders = { id: 123 };
      jest.spyOn(dersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ders });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ders }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(dersService.update).toHaveBeenCalledWith(ders);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ders>>();
      const ders = new Ders();
      jest.spyOn(dersService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ders });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ders }));
      saveSubject.complete();

      // THEN
      expect(dersService.create).toHaveBeenCalledWith(ders);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ders>>();
      const ders = { id: 123 };
      jest.spyOn(dersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ders });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dersService.update).toHaveBeenCalledWith(ders);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMufredatById', () => {
      it('Should return tracked Mufredat primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMufredatById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFormById', () => {
      it('Should return tracked Form primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFormById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackOgretmenById', () => {
      it('Should return tracked Ogretmen primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackOgretmenById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
