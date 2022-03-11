import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DersAnalizService } from '../service/ders-analiz.service';

import { DersAnalizComponent } from './ders-analiz.component';

describe('DersAnaliz Management Component', () => {
  let comp: DersAnalizComponent;
  let fixture: ComponentFixture<DersAnalizComponent>;
  let service: DersAnalizService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DersAnalizComponent],
    })
      .overrideTemplate(DersAnalizComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DersAnalizComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DersAnalizService);

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
    expect(comp.dersAnalizs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
