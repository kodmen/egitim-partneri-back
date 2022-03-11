import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DersAnalizDetailComponent } from './ders-analiz-detail.component';

describe('DersAnaliz Management Detail Component', () => {
  let comp: DersAnalizDetailComponent;
  let fixture: ComponentFixture<DersAnalizDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DersAnalizDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ dersAnaliz: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DersAnalizDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DersAnalizDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dersAnaliz on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.dersAnaliz).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
