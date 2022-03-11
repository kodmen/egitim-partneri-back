import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OgrenciService } from '../service/ogrenci.service';
import { IOgrenci, Ogrenci } from '../ogrenci.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IRozet } from 'app/entities/rozet/rozet.model';
import { RozetService } from 'app/entities/rozet/service/rozet.service';

import { OgrenciUpdateComponent } from './ogrenci-update.component';

describe('Ogrenci Management Update Component', () => {
  let comp: OgrenciUpdateComponent;
  let fixture: ComponentFixture<OgrenciUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ogrenciService: OgrenciService;
  let userService: UserService;
  let rozetService: RozetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OgrenciUpdateComponent],
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
      .overrideTemplate(OgrenciUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OgrenciUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ogrenciService = TestBed.inject(OgrenciService);
    userService = TestBed.inject(UserService);
    rozetService = TestBed.inject(RozetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const ogrenci: IOgrenci = { id: 456 };
      const studentUser: IUser = { id: 1431 };
      ogrenci.studentUser = studentUser;

      const userCollection: IUser[] = [{ id: 48908 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [studentUser];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ogrenci });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Rozet query and add missing value', () => {
      const ogrenci: IOgrenci = { id: 456 };
      const rozetlers: IRozet[] = [{ id: 3117 }];
      ogrenci.rozetlers = rozetlers;

      const rozetCollection: IRozet[] = [{ id: 17390 }];
      jest.spyOn(rozetService, 'query').mockReturnValue(of(new HttpResponse({ body: rozetCollection })));
      const additionalRozets = [...rozetlers];
      const expectedCollection: IRozet[] = [...additionalRozets, ...rozetCollection];
      jest.spyOn(rozetService, 'addRozetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ogrenci });
      comp.ngOnInit();

      expect(rozetService.query).toHaveBeenCalled();
      expect(rozetService.addRozetToCollectionIfMissing).toHaveBeenCalledWith(rozetCollection, ...additionalRozets);
      expect(comp.rozetsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ogrenci: IOgrenci = { id: 456 };
      const studentUser: IUser = { id: 80772 };
      ogrenci.studentUser = studentUser;
      const rozetlers: IRozet = { id: 22504 };
      ogrenci.rozetlers = [rozetlers];

      activatedRoute.data = of({ ogrenci });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(ogrenci));
      expect(comp.usersSharedCollection).toContain(studentUser);
      expect(comp.rozetsSharedCollection).toContain(rozetlers);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ogrenci>>();
      const ogrenci = { id: 123 };
      jest.spyOn(ogrenciService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ogrenci });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ogrenci }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(ogrenciService.update).toHaveBeenCalledWith(ogrenci);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ogrenci>>();
      const ogrenci = new Ogrenci();
      jest.spyOn(ogrenciService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ogrenci });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ogrenci }));
      saveSubject.complete();

      // THEN
      expect(ogrenciService.create).toHaveBeenCalledWith(ogrenci);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ogrenci>>();
      const ogrenci = { id: 123 };
      jest.spyOn(ogrenciService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ogrenci });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ogrenciService.update).toHaveBeenCalledWith(ogrenci);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackRozetById', () => {
      it('Should return tracked Rozet primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRozetById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedRozet', () => {
      it('Should return option if no Rozet is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedRozet(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Rozet for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedRozet(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Rozet is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedRozet(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
