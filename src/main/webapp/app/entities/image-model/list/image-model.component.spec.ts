import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ImageModelService } from '../service/image-model.service';

import { ImageModelComponent } from './image-model.component';

describe('ImageModel Management Component', () => {
  let comp: ImageModelComponent;
  let fixture: ComponentFixture<ImageModelComponent>;
  let service: ImageModelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ImageModelComponent],
    })
      .overrideTemplate(ImageModelComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ImageModelComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ImageModelService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.imageModels?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
