import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OgretmenService } from '../service/ogretmen.service';
import { IOgretmen, Ogretmen } from '../ogretmen.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { OgretmenUpdateComponent } from './ogretmen-update.component';

describe('Ogretmen Management Update Component', () => {
  let comp: OgretmenUpdateComponent;
  let fixture: ComponentFixture<OgretmenUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ogretmenService: OgretmenService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OgretmenUpdateComponent],
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
      .overrideTemplate(OgretmenUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OgretmenUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ogretmenService = TestBed.inject(OgretmenService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const ogretmen: IOgretmen = { id: 456 };
      const ogretmenUser: IUser = { id: 47825 };
      ogretmen.ogretmenUser = ogretmenUser;

      const userCollection: IUser[] = [{ id: 34175 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [ogretmenUser];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ogretmen });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ogretmen: IOgretmen = { id: 456 };
      const ogretmenUser: IUser = { id: 36749 };
      ogretmen.ogretmenUser = ogretmenUser;

      activatedRoute.data = of({ ogretmen });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(ogretmen));
      expect(comp.usersSharedCollection).toContain(ogretmenUser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ogretmen>>();
      const ogretmen = { id: 123 };
      jest.spyOn(ogretmenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ogretmen });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ogretmen }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(ogretmenService.update).toHaveBeenCalledWith(ogretmen);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ogretmen>>();
      const ogretmen = new Ogretmen();
      jest.spyOn(ogretmenService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ogretmen });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ogretmen }));
      saveSubject.complete();

      // THEN
      expect(ogretmenService.create).toHaveBeenCalledWith(ogretmen);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ogretmen>>();
      const ogretmen = { id: 123 };
      jest.spyOn(ogretmenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ogretmen });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ogretmenService.update).toHaveBeenCalledWith(ogretmen);
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
  });
});
