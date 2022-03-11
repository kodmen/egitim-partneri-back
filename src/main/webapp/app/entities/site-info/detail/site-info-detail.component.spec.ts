import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SiteInfoDetailComponent } from './site-info-detail.component';

describe('SiteInfo Management Detail Component', () => {
  let comp: SiteInfoDetailComponent;
  let fixture: ComponentFixture<SiteInfoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SiteInfoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ siteInfo: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SiteInfoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SiteInfoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load siteInfo on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.siteInfo).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
