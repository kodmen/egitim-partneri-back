import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TestAnalizDetailComponent } from './test-analiz-detail.component';

describe('TestAnaliz Management Detail Component', () => {
  let comp: TestAnalizDetailComponent;
  let fixture: ComponentFixture<TestAnalizDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TestAnalizDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ testAnaliz: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TestAnalizDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TestAnalizDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load testAnaliz on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.testAnaliz).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
