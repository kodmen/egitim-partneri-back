import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { KayitService } from '../service/kayit.service';
import { IKayit, Kayit } from '../kayit.model';
import { IDersAnaliz } from 'app/entities/ders-analiz/ders-analiz.model';
import { DersAnalizService } from 'app/entities/ders-analiz/service/ders-analiz.service';
import { IDers } from 'app/entities/ders/ders.model';
import { DersService } from 'app/entities/ders/service/ders.service';
import { IOgrenci } from 'app/entities/ogrenci/ogrenci.model';
import { OgrenciService } from 'app/entities/ogrenci/service/ogrenci.service';

import { KayitUpdateComponent } from './kayit-update.component';

describe('Kayit Management Update Component', () => {
  let comp: KayitUpdateComponent;
  let fixture: ComponentFixture<KayitUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let kayitService: KayitService;
  let dersAnalizService: DersAnalizService;
  let dersService: DersService;
  let ogrenciService: OgrenciService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [KayitUpdateComponent],
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
      .overrideTemplate(KayitUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(KayitUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    kayitService = TestBed.inject(KayitService);
    dersAnalizService = TestBed.inject(DersAnalizService);
    dersService = TestBed.inject(DersService);
    ogrenciService = TestBed.inject(OgrenciService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call DersAnaliz query and add missing value', () => {
      const kayit: IKayit = { id: 456 };
      const dersAnalizleris: IDersAnaliz[] = [{ id: 31014 }];
      kayit.dersAnalizleris = dersAnalizleris;

      const dersAnalizCollection: IDersAnaliz[] = [{ id: 3552 }];
      jest.spyOn(dersAnalizService, 'query').mockReturnValue(of(new HttpResponse({ body: dersAnalizCollection })));
      const additionalDersAnalizs = [...dersAnalizleris];
      const expectedCollection: IDersAnaliz[] = [...additionalDersAnalizs, ...dersAnalizCollection];
      jest.spyOn(dersAnalizService, 'addDersAnalizToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ kayit });
      comp.ngOnInit();

      expect(dersAnalizService.query).toHaveBeenCalled();
      expect(dersAnalizService.addDersAnalizToCollectionIfMissing).toHaveBeenCalledWith(dersAnalizCollection, ...additionalDersAnalizs);
      expect(comp.dersAnalizsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Ders query and add missing value', () => {
      const kayit: IKayit = { id: 456 };
      const aitOldDers: IDers = { id: 28846 };
      kayit.aitOldDers = aitOldDers;

      const dersCollection: IDers[] = [{ id: 19291 }];
      jest.spyOn(dersService, 'query').mockReturnValue(of(new HttpResponse({ body: dersCollection })));
      const additionalDers = [aitOldDers];
      const expectedCollection: IDers[] = [...additionalDers, ...dersCollection];
      jest.spyOn(dersService, 'addDersToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ kayit });
      comp.ngOnInit();

      expect(dersService.query).toHaveBeenCalled();
      expect(dersService.addDersToCollectionIfMissing).toHaveBeenCalledWith(dersCollection, ...additionalDers);
      expect(comp.dersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Ogrenci query and add missing value', () => {
      const kayit: IKayit = { id: 456 };
      const kayitOgrenci: IOgrenci = { id: 46367 };
      kayit.kayitOgrenci = kayitOgrenci;

      const ogrenciCollection: IOgrenci[] = [{ id: 88044 }];
      jest.spyOn(ogrenciService, 'query').mockReturnValue(of(new HttpResponse({ body: ogrenciCollection })));
      const additionalOgrencis = [kayitOgrenci];
      const expectedCollection: IOgrenci[] = [...additionalOgrencis, ...ogrenciCollection];
      jest.spyOn(ogrenciService, 'addOgrenciToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ kayit });
      comp.ngOnInit();

      expect(ogrenciService.query).toHaveBeenCalled();
      expect(ogrenciService.addOgrenciToCollectionIfMissing).toHaveBeenCalledWith(ogrenciCollection, ...additionalOgrencis);
      expect(comp.ogrencisSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const kayit: IKayit = { id: 456 };
      const dersAnalizleris: IDersAnaliz = { id: 48955 };
      kayit.dersAnalizleris = [dersAnalizleris];
      const aitOldDers: IDers = { id: 62271 };
      kayit.aitOldDers = aitOldDers;
      const kayitOgrenci: IOgrenci = { id: 86270 };
      kayit.kayitOgrenci = kayitOgrenci;

      activatedRoute.data = of({ kayit });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(kayit));
      expect(comp.dersAnalizsSharedCollection).toContain(dersAnalizleris);
      expect(comp.dersSharedCollection).toContain(aitOldDers);
      expect(comp.ogrencisSharedCollection).toContain(kayitOgrenci);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Kayit>>();
      const kayit = { id: 123 };
      jest.spyOn(kayitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kayit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: kayit }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(kayitService.update).toHaveBeenCalledWith(kayit);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Kayit>>();
      const kayit = new Kayit();
      jest.spyOn(kayitService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kayit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: kayit }));
      saveSubject.complete();

      // THEN
      expect(kayitService.create).toHaveBeenCalledWith(kayit);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Kayit>>();
      const kayit = { id: 123 };
      jest.spyOn(kayitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kayit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(kayitService.update).toHaveBeenCalledWith(kayit);
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

    describe('trackDersById', () => {
      it('Should return tracked Ders primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDersById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackOgrenciById', () => {
      it('Should return tracked Ogrenci primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackOgrenciById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedDersAnaliz', () => {
      it('Should return option if no DersAnaliz is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedDersAnaliz(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected DersAnaliz for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedDersAnaliz(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this DersAnaliz is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedDersAnaliz(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
