import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormDetailComponent } from './form-detail.component';

describe('Form Management Detail Component', () => {
  let comp: FormDetailComponent;
  let fixture: ComponentFixture<FormDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FormDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ form: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FormDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FormDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load form on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.form).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
