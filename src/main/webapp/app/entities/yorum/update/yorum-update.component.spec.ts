import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { YorumService } from '../service/yorum.service';
import { IYorum, Yorum } from '../yorum.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IForm } from 'app/entities/form/form.model';
import { FormService } from 'app/entities/form/service/form.service';

import { YorumUpdateComponent } from './yorum-update.component';

describe('Yorum Management Update Component', () => {
  let comp: YorumUpdateComponent;
  let fixture: ComponentFixture<YorumUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let yorumService: YorumService;
  let userService: UserService;
  let formService: FormService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [YorumUpdateComponent],
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
      .overrideTemplate(YorumUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(YorumUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    yorumService = TestBed.inject(YorumService);
    userService = TestBed.inject(UserService);
    formService = TestBed.inject(FormService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const yorum: IYorum = { id: 456 };
      const userYorum: IUser = { id: 54254 };
      yorum.userYorum = userYorum;

      const userCollection: IUser[] = [{ id: 94719 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [userYorum];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ yorum });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Form query and add missing value', () => {
      const yorum: IYorum = { id: 456 };
      const formYorum: IForm = { id: 38145 };
      yorum.formYorum = formYorum;

      const formCollection: IForm[] = [{ id: 66275 }];
      jest.spyOn(formService, 'query').mockReturnValue(of(new HttpResponse({ body: formCollection })));
      const additionalForms = [formYorum];
      const expectedCollection: IForm[] = [...additionalForms, ...formCollection];
      jest.spyOn(formService, 'addFormToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ yorum });
      comp.ngOnInit();

      expect(formService.query).toHaveBeenCalled();
      expect(formService.addFormToCollectionIfMissing).toHaveBeenCalledWith(formCollection, ...additionalForms);
      expect(comp.formsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const yorum: IYorum = { id: 456 };
      const userYorum: IUser = { id: 41754 };
      yorum.userYorum = userYorum;
      const formYorum: IForm = { id: 63441 };
      yorum.formYorum = formYorum;

      activatedRoute.data = of({ yorum });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(yorum));
      expect(comp.usersSharedCollection).toContain(userYorum);
      expect(comp.formsSharedCollection).toContain(formYorum);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Yorum>>();
      const yorum = { id: 123 };
      jest.spyOn(yorumService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ yorum });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: yorum }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(yorumService.update).toHaveBeenCalledWith(yorum);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Yorum>>();
      const yorum = new Yorum();
      jest.spyOn(yorumService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ yorum });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: yorum }));
      saveSubject.complete();

      // THEN
      expect(yorumService.create).toHaveBeenCalledWith(yorum);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Yorum>>();
      const yorum = { id: 123 };
      jest.spyOn(yorumService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ yorum });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(yorumService.update).toHaveBeenCalledWith(yorum);
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

    describe('trackFormById', () => {
      it('Should return tracked Form primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFormById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
