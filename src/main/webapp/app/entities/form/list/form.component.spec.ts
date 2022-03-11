import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { FormService } from '../service/form.service';

import { FormComponent } from './form.component';

describe('Form Management Component', () => {
  let comp: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let service: FormService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FormComponent],
    })
      .overrideTemplate(FormComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(FormService);

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
    expect(comp.forms?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
