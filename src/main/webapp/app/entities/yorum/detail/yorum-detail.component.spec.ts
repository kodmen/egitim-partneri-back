import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { YorumDetailComponent } from './yorum-detail.component';

describe('Yorum Management Detail Component', () => {
  let comp: YorumDetailComponent;
  let fixture: ComponentFixture<YorumDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [YorumDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ yorum: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(YorumDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(YorumDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load yorum on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.yorum).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
