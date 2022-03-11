import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MufredatDetailComponent } from './mufredat-detail.component';

describe('Mufredat Management Detail Component', () => {
  let comp: MufredatDetailComponent;
  let fixture: ComponentFixture<MufredatDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MufredatDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ mufredat: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MufredatDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MufredatDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load mufredat on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.mufredat).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
