import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TestAnalizService } from '../service/test-analiz.service';

import { TestAnalizComponent } from './test-analiz.component';

describe('TestAnaliz Management Component', () => {
  let comp: TestAnalizComponent;
  let fixture: ComponentFixture<TestAnalizComponent>;
  let service: TestAnalizService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TestAnalizComponent],
    })
      .overrideTemplate(TestAnalizComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TestAnalizComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TestAnalizService);

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
    expect(comp.testAnalizs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
