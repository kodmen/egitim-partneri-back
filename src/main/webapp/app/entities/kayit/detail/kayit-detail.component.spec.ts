import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KayitDetailComponent } from './kayit-detail.component';

describe('Kayit Management Detail Component', () => {
  let comp: KayitDetailComponent;
  let fixture: ComponentFixture<KayitDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [KayitDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ kayit: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(KayitDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(KayitDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load kayit on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.kayit).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
