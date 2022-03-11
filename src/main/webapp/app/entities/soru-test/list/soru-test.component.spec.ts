import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SoruTestService } from '../service/soru-test.service';

import { SoruTestComponent } from './soru-test.component';

describe('SoruTest Management Component', () => {
  let comp: SoruTestComponent;
  let fixture: ComponentFixture<SoruTestComponent>;
  let service: SoruTestService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SoruTestComponent],
    })
      .overrideTemplate(SoruTestComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SoruTestComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SoruTestService);

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
    expect(comp.soruTests?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
