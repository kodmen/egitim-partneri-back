import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SoruKazanimlariDetailComponent } from './soru-kazanimlari-detail.component';

describe('SoruKazanimlari Management Detail Component', () => {
  let comp: SoruKazanimlariDetailComponent;
  let fixture: ComponentFixture<SoruKazanimlariDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SoruKazanimlariDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ soruKazanimlari: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SoruKazanimlariDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SoruKazanimlariDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load soruKazanimlari on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.soruKazanimlari).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
