import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ImageModelService } from '../service/image-model.service';
import { IImageModel, ImageModel } from '../image-model.model';

import { ImageModelUpdateComponent } from './image-model-update.component';

describe('ImageModel Management Update Component', () => {
  let comp: ImageModelUpdateComponent;
  let fixture: ComponentFixture<ImageModelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let imageModelService: ImageModelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ImageModelUpdateComponent],
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
      .overrideTemplate(ImageModelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ImageModelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    imageModelService = TestBed.inject(ImageModelService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const imageModel: IImageModel = { id: 456 };

      activatedRoute.data = of({ imageModel });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(imageModel));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ImageModel>>();
      const imageModel = { id: 123 };
      jest.spyOn(imageModelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imageModel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imageModel }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(imageModelService.update).toHaveBeenCalledWith(imageModel);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ImageModel>>();
      const imageModel = new ImageModel();
      jest.spyOn(imageModelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imageModel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imageModel }));
      saveSubject.complete();

      // THEN
      expect(imageModelService.create).toHaveBeenCalledWith(imageModel);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ImageModel>>();
      const imageModel = { id: 123 };
      jest.spyOn(imageModelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imageModel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(imageModelService.update).toHaveBeenCalledWith(imageModel);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
